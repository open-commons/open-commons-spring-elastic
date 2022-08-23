/*
 * Copyright 2022 Park Jun-Hong_(parkjunhong77@gmail.com)
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
 * Date  : 2022. 5. 17. 오후 2:11:32
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

package open.commons.spring.elastic.service;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery.OpType;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import open.commons.core.Result;
import open.commons.spring.web.mvc.service.AbstractComponent;

/**
 * 
 * @since 2022. 5. 17.
 * @version 0.2.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 */
public class AbstractElasticsearchService extends AbstractComponent {

    protected final ClientConfiguration esClientConfig;

    protected final RestHighLevelClient highLevelClient;
    @SuppressWarnings("unused")
    protected final RestClient lowLevelClient;

    /**
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 5. 17.		박준홍			최초 작성
     * </pre>
     * 
     * @param esClientConfig
     *            Elasticsearch 클라이언트 설정
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public AbstractElasticsearchService(@NotNull ClientConfiguration esClientConfig) {
        this.esClientConfig = esClientConfig;
        this.highLevelClient = RestClients.create(this.esClientConfig).rest();
        this.lowLevelClient = RestClients.create(this.esClientConfig).lowLevelRest();
    }

    /**
     * 데이터를 Elasticsearch 작업 데이터로 변환하여 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 5. 17.		박준홍			최초 작성
     * </pre>
     *
     * @param <T>
     * @param data
     * @return
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public <T> List<IndexQuery> createBulk(@NotEmpty Collection<T> data) {
        return data.parallelStream().map(d -> {
            return new IndexQueryBuilder() //
                    .withObject(d)//
                    .withOpType(OpType.CREATE) //
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 다수 개의 데이터를 생성하는 작업을 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 5. 17.		박준홍			최초 작성
     * </pre>
     *
     * @param <T>
     * @param data
     * @param type
     * @return
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public <T> Supplier<Result<List<IndexedObjectInformation>>> createBulkIndexAction(List<T> data, Class<T> type) {
        Supplier<Result<List<IndexedObjectInformation>>> action = () -> {
            try {
                ElasticsearchRestTemplate esOp = getElasticsearchOperations();
                List<IndexQuery> queries = createBulk(data);
                return Result.success(esOp.bulkIndex(queries, type));
            } catch (Exception e) {
                return Result.error(e.getMessage());
            }
        };

        return action;
    }

    /**
     * 데이터 삭제하고 결과를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 5. 17.		박준홍			최초 작성
     * </pre>
     *
     * @param query
     * @param clazz
     * @return
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public ByQueryResponse delete(Query query, Class<?> clazz) {
        ElasticsearchRestTemplate esOp = getElasticsearchOperations();
        return esOp.delete(query, clazz);
    }

    /**
     * {@link RestHighLevelClient}를 이용하여 생성된 Elasticsearch 연동 객체를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 5. 17.		박준홍			최초 작성
     * </pre>
     *
     * @return
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public ElasticsearchRestTemplate getElasticsearchOperations() {
        return new ElasticsearchRestTemplate(this.highLevelClient);
    }

    /**
     * 검색된 데이터만 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 5. 17.		박준홍			최초 작성
     * </pre>
     *
     * @param <E>
     * @param query
     * @param type
     * @return
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public <E> List<E> search(Query query, Class<E> type) {
        SearchHits<E> searchHits = searchHits(query, type);
        return searchHits.stream() //
                .map(hit -> hit.getContent())//
                .collect(Collectors.toList());
    }

    /**
     * 데이터를 검색하고 결과를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 5. 17.		박준홍			최초 작성
     * </pre>
     *
     * @param <E>
     * @param query
     * @param type
     * @return
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public <E> SearchHits<E> searchHits(Query query, Class<E> type) {
        ElasticsearchRestTemplate esOp = getElasticsearchOperations();
        return esOp.search(query, type);
    }

}
