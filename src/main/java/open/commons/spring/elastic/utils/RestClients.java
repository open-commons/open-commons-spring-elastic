/*
 * Copyright 2023 Park Jun-Hong_(parkjunhong77@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 *
 * This file is generated under this project, "open-commons-spring-elastic".
 *
 * Date  : 2023. 10. 13. 오후 4:05:35
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

// Original Copyright is below.
/*
 * Copyright 2018-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package open.commons.spring.elastic.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.ClientLogger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;

/**
 * 
 * This class is based from {@link org.springframework.data.elasticsearch.client.RestClients} to support
 * {@link RestClient} and {@link ElasticsearchClient}.
 * 
 * <p>
 * Original API is below.
 * 
 * <pre>
 * Utility class for common access to Elasticsearch clients. {@link RestClients} consolidates set up routines for the
 * various drivers into a single place.
 *
 * &#64;author Christoph Strobl
 * &#64;author Mark Paluch
 * &#64;author Huw Ayling-Miller
 * &#64;author Henrique Amaral
 * &#64;author Peter-Josef Meisch
 * &#64;author Nic Hines
 * &#64;since 3.2
 * </pre>
 * 
 * </p>
 * 
 * @since 2023. 10. 13.
 * @version 0.3.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 */
public class RestClients {

    /**
     * Name of whose value can be used to correlate log messages for this request.
     */
    private static final String LOG_ID_ATTRIBUTE = RestClients.class.getName() + ".LOG_ID";

    private RestClients() {
    }

    /**
     * Start here to create a new client tailored to your needs.
     *
     * @return new instance of {@link RestClient}.
     */
    public static RestClient create(ClientConfiguration clientConfiguration) {

        Assert.notNull(clientConfiguration, "ClientConfiguration must not be null!");

        HttpHost[] httpHosts = formattedHosts(clientConfiguration.getEndpoints(), clientConfiguration.useSsl()).stream().map(HttpHost::create).toArray(HttpHost[]::new);
        RestClientBuilder builder = RestClient.builder(httpHosts);

        if (clientConfiguration.getPathPrefix() != null) {
            builder.setPathPrefix(clientConfiguration.getPathPrefix());
        }

        HttpHeaders headers = clientConfiguration.getDefaultHeaders();

        if (!headers.isEmpty()) {
            builder.setDefaultHeaders(toHeaderArray(headers));
        }

        builder.setHttpClientConfigCallback(clientBuilder -> {
            clientConfiguration.getSslContext().ifPresent(clientBuilder::setSSLContext);
            clientConfiguration.getHostNameVerifier().ifPresent(clientBuilder::setSSLHostnameVerifier);
            clientBuilder.addInterceptorLast(new CustomHeaderInjector(clientConfiguration.getHeadersSupplier()));

            if (ClientLogger.isEnabled()) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

                clientBuilder.addInterceptorLast((HttpRequestInterceptor) interceptor);
                clientBuilder.addInterceptorLast((HttpResponseInterceptor) interceptor);
            }

            Builder requestConfigBuilder = RequestConfig.custom();
            Duration connectTimeout = clientConfiguration.getConnectTimeout();

            if (!connectTimeout.isNegative()) {
                requestConfigBuilder.setConnectTimeout(Math.toIntExact(connectTimeout.toMillis()));
            }

            Duration socketTimeout = clientConfiguration.getSocketTimeout();

            if (!socketTimeout.isNegative()) {
                requestConfigBuilder.setSocketTimeout(Math.toIntExact(socketTimeout.toMillis()));
                requestConfigBuilder.setConnectionRequestTimeout(Math.toIntExact(socketTimeout.toMillis()));
            }

            clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());

            clientConfiguration.getProxy().map(HttpHost::create).ifPresent(clientBuilder::setProxy);

            for (ClientConfiguration.ClientConfigurationCallback<?> clientConfigurer : clientConfiguration.getClientConfigurers()) {
                if (clientConfigurer instanceof RestClientConfigurationCallback) {
                    RestClientConfigurationCallback restClientConfigurationCallback = (RestClientConfigurationCallback) clientConfigurer;
                    clientBuilder = restClientConfigurationCallback.configure(clientBuilder);
                }
            }

            return clientBuilder;
        });

        return builder.build();
    }

    /**
     * {@link ElasticsearchAsyncClient}를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 18.		박준홍			최초 작성
     * </pre>
     *
     * @param transport
     * @return
     *
     * @since 2023. 10. 18.
     * @version 0.3.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public static ElasticsearchAsyncClient createElasticsearchAsyncClient(ElasticsearchTransport transport) {
        return createElasticsearchAsyncClient(transport, null);
    }

    /**
     * {@link ElasticsearchAsyncClient}를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 18.		박준홍			최초 작성
     * </pre>
     *
     * @param transport
     * @param transportOptions
     * @return
     *
     * @since 2023. 10. 18.
     * @version 0.3.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public static ElasticsearchAsyncClient createElasticsearchAsyncClient(ElasticsearchTransport transport, @Nullable TransportOptions transportOptions) {
        return new ElasticsearchAsyncClient(transport, transportOptions);
    }

    /**
     * {@link ElasticsearchAsyncClient}를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 18.		박준홍			최초 작성
     * </pre>
     *
     * @param restClient
     * @param mapper
     * @return
     *
     * @since 2023. 10. 18.
     * @version 0.3.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public static ElasticsearchAsyncClient createElasticsearchAsyncClient(RestClient restClient, JsonpMapper mapper) {
        return createElasticsearchAsyncClient(restClient, mapper, null);
    }

    /**
     * {@link ElasticsearchAsyncClient}를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 18.		박준홍			최초 작성
     * </pre>
     *
     * @param restClient
     * @param mapper
     * @param transportOptions
     * @return
     *
     * @since 2023. 10. 18.
     * @version 0.3.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public static ElasticsearchAsyncClient createElasticsearchAsyncClient(RestClient restClient, JsonpMapper mapper, @Nullable TransportOptions transportOptions) {
        if (mapper == null) {
            mapper = new JacksonJsonpMapper(new ObjectMapper());
        }

        ElasticsearchTransport transport = new RestClientTransport(restClient, mapper, transportOptions);
        return createElasticsearchAsyncClient(transport);
    }

    /**
     * {@link ElasticsearchClient} 를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 13.		박준홍			최초 작성
     * </pre>
     *
     * @param transport
     * @return
     *
     * @since 2023. 10. 13.
     * @version 0.3.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public static ElasticsearchClient createElasticsearchClient(ElasticsearchTransport transport) {
        return createElasticsearchClient(transport, null);
    }

    /**
     * {@link ElasticsearchClient} 를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 13.		박준홍			최초 작성
     * </pre>
     *
     * @param transport
     * @param transportOptions
     * @return
     *
     * @since 2023. 10. 13.
     * @version 0.3.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public static ElasticsearchClient createElasticsearchClient(ElasticsearchTransport transport, @Nullable TransportOptions transportOptions) {
        return new ElasticsearchClient(transport, transportOptions);
    }

    /**
     * {@link ElasticsearchClient} 를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 13.		박준홍			최초 작성
     * </pre>
     *
     * @param restClient
     * @param mapper
     * @return
     *
     * @since 2023. 10. 13.
     * @version 0.3.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public static ElasticsearchClient createElasticsearchClient(RestClient restClient, JsonpMapper mapper) {
        return createElasticsearchClient(restClient, mapper, null);
    }

    /**
     * {@link ElasticsearchClient} 를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 13.		박준홍			최초 작성
     * </pre>
     *
     * @param restClient
     * @param mapper
     * @param transportOptions
     * @return
     *
     * @since 2023. 10. 13.
     * @version 0.3.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public static ElasticsearchClient createElasticsearchClient(RestClient restClient, JsonpMapper mapper, @Nullable TransportOptions transportOptions) {
        if (mapper == null) {
            mapper = new JacksonJsonpMapper(new ObjectMapper());
        }

        ElasticsearchTransport transport = new RestClientTransport(restClient, mapper, transportOptions);
        return createElasticsearchClient(transport);
    }

    private static List<String> formattedHosts(List<InetSocketAddress> hosts, boolean useSsl) {
        return hosts.stream().map(it -> (useSsl ? "https" : "http") + "://" + it.getHostString() + ":" + it.getPort()).collect(Collectors.toList());
    }

    private static Header[] toHeaderArray(HttpHeaders headers) {
        return headers.entrySet().stream() //
                .flatMap(entry -> entry.getValue().stream() //
                        .map(value -> new BasicHeader(entry.getKey(), value))) //
                .toArray(Header[]::new);
    }

    /**
     * Interceptor to inject custom supplied headers.
     *
     * @since 4.0
     */
    private static class CustomHeaderInjector implements HttpRequestInterceptor {

        private final Supplier<HttpHeaders> headersSupplier;

        public CustomHeaderInjector(Supplier<HttpHeaders> headersSupplier) {
            this.headersSupplier = headersSupplier;
        }

        @Override
        public void process(HttpRequest request, HttpContext context) {
            HttpHeaders httpHeaders = headersSupplier.get();

            if (httpHeaders != null && httpHeaders != HttpHeaders.EMPTY) {
                Arrays.stream(toHeaderArray(httpHeaders)).forEach(request::addHeader);
            }
        }
    }

    /**
     * Logging interceptors for Elasticsearch client logging.
     *
     * @see ClientLogger
     * @since 3.2
     */
    private static class HttpLoggingInterceptor implements HttpResponseInterceptor, HttpRequestInterceptor {

        @Override
        public void process(HttpRequest request, HttpContext context) throws IOException {

            String logId = (String) context.getAttribute(RestClients.LOG_ID_ATTRIBUTE);

            if (logId == null) {
                logId = ClientLogger.newLogId();
                context.setAttribute(RestClients.LOG_ID_ATTRIBUTE, logId);
            }

            if (request instanceof HttpEntityEnclosingRequest && ((HttpEntityEnclosingRequest) request).getEntity() != null) {

                HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                entity.writeTo(buffer);

                if (!entity.isRepeatable()) {
                    entityRequest.setEntity(new ByteArrayEntity(buffer.toByteArray()));
                }

                ClientLogger.logRequest(logId, request.getRequestLine().getMethod(), request.getRequestLine().getUri(), "", buffer::toString);
            } else {
                ClientLogger.logRequest(logId, request.getRequestLine().getMethod(), request.getRequestLine().getUri(), "");
            }
        }

        @Override
        public void process(HttpResponse response, HttpContext context) {
            String logId = (String) context.getAttribute(RestClients.LOG_ID_ATTRIBUTE);
            ClientLogger.logRawResponse(logId, HttpStatus.resolve(response.getStatusLine().getStatusCode()));
        }
    }

    /**
     * {@link org.springframework.data.elasticsearch.client.ClientConfiguration.ClientConfigurationCallback} to
     * configure the RestClient with a {@link HttpAsyncClientBuilder}
     *
     * @since 4.3
     */
    public interface RestClientConfigurationCallback extends ClientConfiguration.ClientConfigurationCallback<HttpAsyncClientBuilder> {

        static RestClientConfigurationCallback from(Function<HttpAsyncClientBuilder, HttpAsyncClientBuilder> clientBuilderCallback) {

            Assert.notNull(clientBuilderCallback, "clientBuilderCallback must not be null");

            // noinspection NullableProblems
            return clientBuilderCallback::apply;
        }
    }

}
