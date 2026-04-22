/*
 * Copyright 2024 Park Jun-Hong (parkjunhong77@gmail.com)
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
 * Date  : 2024. 4. 11. 오후 1:54:57
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

package open.commons.spring.elastic.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import jakarta.validation.constraints.NotBlank;

import org.springframework.data.elasticsearch.client.ClientConfiguration;

import open.commons.core.Result;
import open.commons.core.utils.AssertUtils2;
import open.commons.spring.elastic.utils.RestClients;
import open.commons.spring.web.mvc.service.AbstractComponent;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;

/**
 * Elasticsearch Java API 기능을 지원하는 클래스. <br>
 * 
 * <pre>
 * [개정이력]
 *      날짜      |   작성자                 |	내용
 * -------------------------------------------------
 * 2022. 5. 17.     parkjunhong77@gmail.com     최초 작성
 * 2023. 10. 13.    parkjunhong77@gmail.com     Migrate from the High Level Rest Client to Java API Client.
 * 2024. 4. 11.     parkjunhong77@gmail.com     {@link AbstractElasticsearchService}에서 Elasticsearch Java API Client 기능을 분리.
 * 2026. 4. 22.     parkjunhong77@gmail.com     Spring Boot:2.7.15 -> 4.0.3, Spring Framework: 5.3.29 -> 7.0.5.
 * </pre>
 * 
 * @since 2024. 4. 11.
 * @version 0.3.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 */
public abstract class AbstractElasticClientService extends AbstractComponent {

    protected final Rest5Client restClient;
    protected final ElasticsearchClient esClient;
    protected final ElasticsearchAsyncClient esAsyncClient;

    /**
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2024. 4. 11.		parkjunhong77@gmail.com			최초 작성
     * </pre>
     *
     * @param esClientConfig
     *
     * @since 2024. 4. 11.
     * @version 0.3.0
     */
    public AbstractElasticClientService(ClientConfiguration esClientConfig) {
        AssertUtils2.notNull(esClientConfig);

        this.restClient = RestClients.create(esClientConfig);
        this.esClient = createElasticsearchClient(this.restClient);
        this.esAsyncClient = createElasticsearchAsyncClient(this.restClient);
    }

    /**
     * {@link ElasticsearchAsyncClient}를 제공합니다.<br>
     * 하위 클래스는 이 메소드를 overriding 하여 목적에 맞게 구현합니다.
     * 
     * <pre>
     * [개정이력]
     *      날짜      | 작성자   |   내용
     * ------------------------------------------
     * 2023. 10. 18.    parkjunhong77@gmail.com     최초 작성
     * 2024. 4. 11.		parkjunhong77@gmail.com		{@link AbstractElasticsearchService#createElasticsearchAsyncClient(org.springframework.web.client.RestClient)}에서 이관.
     * 2026. 4. 22.     parkjunhong77@gmail.com     파라미터 변경. {@link org.springframework.web.client.RestClient}::5.3.29 -> {@link Rest5Client}:7.0.5
     * </pre>
     *
     * @param restClient
     * @return
     *
     * @since 2023. 10. 18.
     * @version 0.3.0
     */
    protected ElasticsearchAsyncClient createElasticsearchAsyncClient(Rest5Client restClient) {
        return RestClients.createElasticsearchAsyncClient(restClient, null);
    }

    /**
     * {@link ElasticsearchClient}를 제공합니다.<br>
     * 하위 클래스는 이 메소드를 overriding 하여 목적에 맞게 구현합니다.
     * 
     * <pre>
     * [개정이력]
     *      날짜      | 작성자   |   내용
     * ------------------------------------------
     * 2023. 10. 16.    parkjunhong77@gmail.com     최초 작성
     * 2024. 4. 11.     parkjunhong77@gmail.com     {@link AbstractElasticsearchService#createElasticsearchClient(org.springframework.web.client.RestClient)}에서 이관.
     * 2026. 4. 22.     parkjunhong77@gmail.com     파라미터 변경. {@link org.springframework.web.client.RestClient}::5.3.29 -> {@link Rest5Client}:7.0.5
     * </pre>
     *
     * @param restClient
     * @return
     *
     * @since 2023. 10. 16.
     * @version 0.2.0
     */
    protected ElasticsearchClient createElasticsearchClient(Rest5Client restClient) {
        return RestClients.createElasticsearchClient(restClient, null);
    }

    /**
     * 주어진 이름과 매핑 정보를 이용하여 Index 를 생성합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜      | 작성자   |   내용
     * ------------------------------------------
     * 2022. 10. 13.    parkjunhong77@gmail.com     최초 작성
     * 2023. 10. 13.    parkjunhong77@gmail.com     Migrate from the High Level Rest Client to Java API Client.
     * 2024. 4. 11.     parkjunhong77@gmail.com     {@link AbstractElasticsearchService#createElasticsearchClient(org.springframework.web.client.RestClient)}에서 이관.
     * </pre>
     *
     * @param indexName
     * @param source
     *            'settings' and 'mapping' 문자열
     * @return 'index' 생성 결과
     *
     * @since 2022. 10. 13.
     * @version 0.2.0
     */
    public Result<String> createIndex(@NotBlank String indexName, @NotBlank String source) {
        AssertUtils2.notBlanks(indexName, source);

        try {
            ElasticsearchIndicesClient idxClient = this.esClient.indices();
            BooleanResponse res = idxClient.exists(bld -> bld.index(indexName));
            boolean exists = res.value();

            if (exists) {
                logger.debug("* * * '{}' exist. index={}", exists ? "ALREADY" : "DO NOT", indexName);
                return Result.success(indexName);
            } else {
                if (source != null) {
                    Reader sourceReader = new StringReader(source);
                    CreateIndexResponse resCreateIndex = idxClient
                            .create(b -> b.index(indexName).withJson(sourceReader));
                    logger.info("* * * 'CREATE' an index, {}. info={}", indexName, resCreateIndex);
                    return Result.success(indexName);
                } else {
                    String failedMsg = String.format("* * * 'No' source(settins, mappings, etc) for %s. info=%s",
                            indexName, source);
                    logger.warn("{}", failedMsg);
                    return Result.error(failedMsg);
                }
            }
        } catch (ElasticsearchException e) {
            if (e.error() != null && "resource_already_exists_exception".equals(e.error().type())) {
                logger.debug("* * * '{}' ALREADY exist. index={}", indexName);
                return Result.success(indexName);
            } else {
                String errMsg = String.format("'%s' index 조회/생성 시 오류가 발생하였습니다. 원인=%s", indexName, e.getMessage());
                logger.error("{}", errMsg, e);
                return Result.error(errMsg);
            }
        } catch (IOException e) {
            String errMsg = String.format("'%s' index 조회/생성 시 오류가 발생하였습니다. 원인=%s", indexName, e.getMessage());
            logger.error("{}", errMsg, e);
            return Result.error(errMsg);
        }
    }
}
