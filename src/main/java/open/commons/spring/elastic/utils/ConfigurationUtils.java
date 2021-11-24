/*
 * Copyright 2021 Park Jun-Hong_(parkjunhong77@gmail.com)
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
 * Date  : 2021. 11. 15. 오후 5:01:50
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

package open.commons.spring.elastic.utils;

import java.util.function.Function;

import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.ClientConfiguration.MaybeSecureClientConfigurationBuilder;
import org.springframework.data.elasticsearch.client.ClientConfiguration.TerminalClientConfigurationBuilder;

import open.commons.spring.elastic.configuration.elasticsearch.ElasticsearchClientBuilderConfiguration;
import open.commons.spring.elastic.configuration.elasticsearch.EndpointBuilderConfiguration;
import open.commons.spring.elastic.configuration.elasticsearch.SccBuilderConfiguration;
import open.commons.spring.elastic.configuration.elasticsearch.TccBuilderConfiguration;
import open.commons.utils.FunctionUtils;

/**
 * 설정 편의 기능을 제공하는 클래스.
 * 
 * @since 2021. 11. 15.
 * @version 0.1.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 */
public class ConfigurationUtils {
    private ConfigurationUtils() {
    }

    /**
     * Elasticsearch 클라이언트 설정 정보를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2021. 11. 15.		박준홍			최초 작성
     * </pre>
     *
     * @param <E>
     *            {@link EndpointBuilderConfiguration}
     * @param <S>
     *            {@link SccBuilderConfiguration}
     * @param <T>
     *            {@link TccBuilderConfiguration}
     * @param builderConfiguration
     *            Elasticsearch 클라이언트 빌더 설정 정보
     * @return
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public static <E extends EndpointBuilderConfiguration, S extends SccBuilderConfiguration, T extends TccBuilderConfiguration> ClientConfiguration createClientConfiguration(
            ElasticsearchClientBuilderConfiguration<E, S, T> builderConfiguration) {

        EndpointBuilderConfiguration ebc = builderConfiguration.getEndpoint();
        SccBuilderConfiguration sbc = builderConfiguration.getSecure();
        TccBuilderConfiguration tbc = builderConfiguration.getTerminal();

        // begin - EndpointConfiguration
        TerminalClientConfigurationBuilder builder = ClientConfiguration.builder()//
                .connectedTo(ebc.getConnections().toArray(new String[0])) //
        ;
        // end - EndpointConfiguration

        // begin - Secure Client Configuration
        if (sbc != null && sbc.isSsl()) {
            builder = ((MaybeSecureClientConfigurationBuilder) builder).usingSsl();
        }
        // end - Secure Client Configuration

        // begin - Terminal Client Configuration
        if (tbc.usableBasicAuth()) {
            builder = builder.withBasicAuth(tbc.getUsername(), tbc.getPassword());
        }

        builder = setConfiguration(tbc.getConnectTimeout(), builder::withConnectTimeout, builder);
        builder = setConfiguration(tbc.getDefaultHeaders(), builder::withDefaultHeaders, builder);
        builder = setConfiguration(tbc.getPathPrefix(), builder::withPathPrefix, builder);
        builder = setConfiguration(tbc.getProxy(), builder::withProxy, builder);
        builder = setConfiguration(tbc.getSocketTimeout(), builder::withSocketTimeout, builder);
        // end - Terminal Client Configuration
        return builder.build();
    }

    private static <T> TerminalClientConfigurationBuilder setConfiguration(T config, Function<T, TerminalClientConfigurationBuilder> setter,
            TerminalClientConfigurationBuilder defaultValue) {
        if (config instanceof String) {
            return FunctionUtils.runIf(config, o -> o != null && !((String) o).trim().isEmpty(), setter, defaultValue);
        } else {
            return FunctionUtils.runIf(config, o -> o != null, setter, defaultValue);
        }
    }
}
