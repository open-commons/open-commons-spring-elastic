/*
 * Copyright 2023 Park Jun-Hong (parkjunhong77@gmail.com)
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
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.EntityDetails;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpResponseInterceptor;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.Timeout;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.rest5_client.Rest5Clients;
import org.springframework.data.elasticsearch.support.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.Jackson3JsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportOptions;
import co.elastic.clients.transport.rest5_client.Rest5ClientOptions;
import co.elastic.clients.transport.rest5_client.Rest5ClientTransport;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;
import co.elastic.clients.transport.rest5_client.low_level.Rest5ClientBuilder;
import tools.jackson.databind.json.JsonMapper;

/**
 * 
 * This class is based from {@link Rest5Clients} to support {@link ElasticsearchAsyncClient} and
 * {@link ElasticsearchClient}.
 * 
 * 
 * <pre>
 * [개정이력]
 *      날짜       | 작성자                   |   내용
 * -----------------------------------------------------
 * 2023. 10. 13.    parkjunhong77@gmail.com     최초 작성
 * 2026. 4. 22.     parkjunhong77@gmail.com     Spring Boot:2.7.15 -> 4.0.3, Spring Framework: 5.3.29 -> 7.0.5.
 * </pre>
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
    public static Rest5Client create(ClientConfiguration clientConfiguration) {
        Assert.notNull(clientConfiguration, "ClientConfiguration must not be null!");

        HttpHost[] httpHosts = formattedHosts(clientConfiguration.getEndpoints(), clientConfiguration.useSsl()).stream()
                .map(t -> {
                    try {
                        return HttpHost.create(t);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }).toArray(HttpHost[]::new);

        Rest5ClientBuilder builder = Rest5Client.builder(httpHosts);

        if (clientConfiguration.getPathPrefix() != null) {
            builder.setPathPrefix(clientConfiguration.getPathPrefix());
        }

        HttpHeaders headers = clientConfiguration.getDefaultHeaders();
        if (!headers.isEmpty()) {
            builder.setDefaultHeaders(toHeaderArray(headers));
        }

        // 🎯 Rest5ClientBuilder 규격에 맞춘 Consumer 적용
        Consumer<HttpAsyncClientBuilder> httpClientConfigCallback = clientBuilder -> {

            // ====================================================================
            // 🎯 STEP 1. Connection Manager 설정 (TLS & Connect Timeout 전담)
            // ====================================================================
            PoolingAsyncClientConnectionManagerBuilder connManagerBuilder = PoolingAsyncClientConnectionManagerBuilder
                    .create();

            // 1-1. SSL / TLS 설정 (SDE 6.0 스펙에 따라 HostnameVerifier는 무시하고 SSLContext만 적용)
            clientConfiguration.getSslContext().ifPresent(sslContext -> {
                TlsStrategy tlsStrategy = ClientTlsStrategyBuilder.create().setSslContext(sslContext).buildAsync();
                connManagerBuilder.setTlsStrategy(tlsStrategy);
            });

            // 1-2. Connect Timeout 설정 (HC5 최신 방식: ConnectionConfig 사용)
            Duration connectTimeout = clientConfiguration.getConnectTimeout();
            if (!connectTimeout.isNegative()) {
                ConnectionConfig connectionConfig = ConnectionConfig.custom()
                        .setConnectTimeout(Timeout.ofMilliseconds(connectTimeout.toMillis())).build();
                connManagerBuilder.setDefaultConnectionConfig(connectionConfig);
            }

            // 🎯 완성된 Connection Manager를 ClientBuilder에 장착!
            clientBuilder.setConnectionManager(connManagerBuilder.build());

            // ====================================================================
            // 🎯 STEP 2. Interceptor & 기타 설정
            // ====================================================================

            // 2-1. 인터셉터 등록
            clientBuilder.addRequestInterceptorLast(new CustomHeaderInjector(clientConfiguration.getHeadersSupplier()));

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            clientBuilder.addRequestInterceptorLast(interceptor);
            clientBuilder.addResponseInterceptorLast(interceptor);

            // 2-2. Response & Socket Timeout 설정 (이 항목들은 여전히 RequestConfig 담당입니다)
            RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
            Duration socketTimeout = clientConfiguration.getSocketTimeout();

            if (!socketTimeout.isNegative()) {
                // 서버로부터 응답을 기다리는 최대 시간 (Socket Timeout 대체)
                requestConfigBuilder.setResponseTimeout(Timeout.ofMilliseconds(socketTimeout.toMillis()));
                // 커넥션 풀에서 커넥션을 획득하기까지 기다리는 최대 시간
                requestConfigBuilder.setConnectionRequestTimeout(Timeout.ofMilliseconds(socketTimeout.toMillis()));
            }

            clientBuilder.setDefaultRequestConfig(requestConfigBuilder.build());

            // 2-3. 프록시 설정
            clientConfiguration.getProxy().map(proxyStr -> {
                try {
                    return HttpHost.create(proxyStr);
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("잘못된 Proxy URI 포맷입니다: " + proxyStr, e);
                }
            }).ifPresent(clientBuilder::setProxy);
        };

        builder.setHttpClientConfigCallback(httpClientConfigCallback);

        return builder.build();
    }

    /**
     * {@link ElasticsearchAsyncClient}를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 18.		parkjunhong77@gmail.com			최초 작성
     * </pre>
     *
     * @param transport
     * @return
     *
     * @since 2023. 10. 18.
     * @version 0.3.0
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
     * 2023. 10. 18.		parkjunhong77@gmail.com			최초 작성
     * </pre>
     *
     * @param transport
     * @param transportOptions
     * @return
     *
     * @since 2023. 10. 18.
     * @version 0.3.0
     */
    public static ElasticsearchAsyncClient createElasticsearchAsyncClient(ElasticsearchTransport transport,
            @Nullable TransportOptions transportOptions) {
        return new ElasticsearchAsyncClient(transport, transportOptions);
    }

    /**
     * {@link ElasticsearchAsyncClient}를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 18.		parkjunhong77@gmail.com			최초 작성
     * </pre>
     *
     * @param restClient
     * @param mapper
     * @return
     *
     * @since 2023. 10. 18.
     * @version 0.3.0
     */
    public static ElasticsearchAsyncClient createElasticsearchAsyncClient(Rest5Client restClient, JsonpMapper mapper) {
        return createElasticsearchAsyncClient(restClient, mapper, null);
    }

    /**
     * Elastic 9.2.5 및 HttpClient 5 기반의 비동기 클라이언트를 생성합니다. * @param restClient Apache HttpClient 5
     * 기반의 저수준 클라이언트 (Rest5Client)
     * 
     * <pre>
     * [개정이력]
     *      날짜       | 작성자                   |   내용
     * -----------------------------------------------------
     * 2026. 4. 22.		박준홍(jhpark@ymtech.co.kr)		최초 작성
     * </pre>
     * 
     * @param mapper
     *            Jackson 3를 지원하는 매퍼 (null일 경우 Jackson3JsonpMapper 생성)
     * @param rest5ClientOptions
     *            트랜스포트 옵션
     * @return {@link ElasticsearchAsyncClient}
     */
    public static ElasticsearchAsyncClient createElasticsearchAsyncClient(Rest5Client restClient,
            @Nullable JsonpMapper mapper, @Nullable Rest5ClientOptions rest5ClientOptions) {

        // 1. Jackson 3 (Tools Jackson) 매퍼 설정
        if (mapper == null) {
            // Spring Boot 4.0.3의 tools.jackson.databind.ObjectMapper를 사용합니다.
            mapper = new Jackson3JsonpMapper(new JsonMapper());
        }

        // 2. Rest5Client(HC5) 전용 트랜스포트 생성
        ElasticsearchTransport transport = new Rest5ClientTransport(restClient, mapper, rest5ClientOptions);

        // 3. 고수준 비동기 클라이언트 반환
        return new ElasticsearchAsyncClient(transport);
    }

    /**
     * {@link ElasticsearchClient} 를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 13.		parkjunhong77@gmail.com			최초 작성
     * </pre>
     *
     * @param transport
     * @return
     *
     * @since 2023. 10. 13.
     * @version 0.3.0
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
     * 2023. 10. 13.		parkjunhong77@gmail.com			최초 작성
     * </pre>
     *
     * @param transport
     * @param transportOptions
     * @return
     *
     * @since 2023. 10. 13.
     * @version 0.3.0
     */
    public static ElasticsearchClient createElasticsearchClient(ElasticsearchTransport transport,
            @Nullable TransportOptions transportOptions) {
        return new ElasticsearchClient(transport, transportOptions);
    }

    /**
     * {@link ElasticsearchClient} 를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 13.		parkjunhong77@gmail.com			최초 작성
     * </pre>
     *
     * @param restClient
     * @param mapper
     * @return
     *
     * @since 2023. 10. 13.
     * @version 0.3.0
     */
    public static ElasticsearchClient createElasticsearchClient(Rest5Client restClient, JsonpMapper mapper) {
        return createElasticsearchClient(restClient, mapper, null);
    }

    /**
     * {@link ElasticsearchClient} 를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2023. 10. 13.		parkjunhong77@gmail.com			최초 작성
     * </pre>
     *
     * @param restClient
     * @param mapper
     * @param rest5ClientOptions
     * @return
     *
     * @since 2023. 10. 13.
     * @version 0.3.0
     */
    public static ElasticsearchClient createElasticsearchClient(Rest5Client restClient, JsonpMapper mapper,
            @Nullable Rest5ClientOptions rest5ClientOptions) {
        // 1. Jackson 3 (Tools Jackson) 매퍼 설정
        if (mapper == null) {
            // Spring Boot 4.0.3의 tools.jackson.databind.ObjectMapper를 사용합니다.
            mapper = new Jackson3JsonpMapper(new JsonMapper());
        }

        // 2. Rest5Client(HC5) 전용 트랜스포트 생성
        ElasticsearchTransport transport = new Rest5ClientTransport(restClient, mapper, rest5ClientOptions);

        return new ElasticsearchClient(transport);
    }

    private static List<String> formattedHosts(List<InetSocketAddress> hosts, boolean useSsl) {
        return hosts.stream().map(it -> (useSsl ? "https" : "http") + "://" + it.getHostString() + ":" + it.getPort())
                .collect(Collectors.toList());
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

        /**
         * {@inheritDoc}
         *
         * @since 2026. 4. 22.
         * @version 4.0.0
         *
         * @see org.apache.hc.core5.http.HttpRequestInterceptor#process(org.apache.hc.core5.http.HttpRequest,
         *      org.apache.hc.core5.http.EntityDetails,
         *      org.apache.hc.core5.http.protocol.HttpContext)
         */
        @Override
        public void process(HttpRequest request, EntityDetails entity, HttpContext context)
                throws HttpException, IOException {
            HttpHeaders httpHeaders = headersSupplier.get();

            if (httpHeaders != null) {
                Arrays.stream(toHeaderArray(httpHeaders)).forEach(request::addHeader);
            }
        }
    }

    /**
     * Logging interceptors for Elasticsearch client logging (HC5).
     */
    private static class HttpLoggingInterceptor implements HttpRequestInterceptor, HttpResponseInterceptor {

        private static final Logger log = LoggerFactory.getLogger(HttpLoggingInterceptor.class);

        @Override
        public void process(HttpRequest request, EntityDetails entityDetails, HttpContext context)
                throws HttpException, IOException {

            if (!log.isDebugEnabled()) {
                return;
            }

            // 1. Log ID 관리 (HttpContext 활용)
            String logId = (String) context.getAttribute(RestClients.LOG_ID_ATTRIBUTE);
            if (logId == null) {
                logId = UUID.randomUUID().toString().substring(0, 8);
                context.setAttribute(RestClients.LOG_ID_ATTRIBUTE, logId);
            }

            // 2. Request Entity 로깅 및 재구성 (Java 25 Pattern Matching 적용)
            if (request instanceof HttpEntityContainer container && container.getEntity() != null) {
                HttpEntity entity = container.getEntity();

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                entity.writeTo(buffer);

                // 소비된 엔티티가 다시 읽을 수 없는 경우(Non-repeatable), 복사본으로 교체합니다.
                if (!entity.isRepeatable()) {
                    ContentType contentType = ContentType.parse(entity.getContentType());
                    // HC5의 ByteArrayEntity는 byte[]와 ContentType을 인자로 받습니다.
                    container.setEntity(new ByteArrayEntity(buffer.toByteArray(), contentType));
                }

                log.debug("[{}] Request: {} {} | Body: {}", logId, request.getMethod(), request.getRequestUri(),
                        buffer.toString());
            } else {
                log.debug("[{}] Request: {} {}", logId, request.getMethod(), request.getRequestUri());
            }
        }

        @Override
        public void process(HttpResponse response, EntityDetails entityDetails, HttpContext context)
                throws HttpException, IOException {

            if (!log.isDebugEnabled()) {
                return;
            }

            String logId = (String) context.getAttribute(RestClients.LOG_ID_ATTRIBUTE);

            // HC5: response.getStatusLine().getStatusCode() 대신 getCode()를 직접 사용합니다.
            int statusCode = response.getCode();
            log.debug("[{}] Response: {}", logId, HttpStatus.resolve(statusCode));
        }
    }

    public interface RestClientConfigurationCallback
            extends ClientConfiguration.ClientConfigurationCallback<HttpAsyncClientBuilder> {
        static RestClientConfigurationCallback from(
                Function<HttpAsyncClientBuilder, HttpAsyncClientBuilder> clientBuilderCallback) {
            Assert.notNull(clientBuilderCallback, "clientBuilderCallback must not be null");
            return clientBuilderCallback::apply;
        }
    }

}
