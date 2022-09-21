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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Arrays;
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
import open.commons.core.utils.IOUtils;
import open.commons.spring.elastic.utils.RestApiUtils;
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
     * Elasticsearch JSON 으로 저장된 파일을 한번에 저장합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 9. 15.		박준홍			최초 작성
     * </pre>
     *
     * @param url
     *            Elasticsearch API 'Bulk Index' URL.<br>
     *            <ul>
     *            <li>포맷: {scheme}://{host}:{port}/{index}/_bulk
     *            <li>예시: http://192.168.0.10:80/test-index/_bulk
     *            </ul>
     * @param filepath
     *            Elasticsearch JSON bulk 파일 절대경로
     * @throws IOException
     * @throws InterruptedException
     *
     * @since 2022. 9. 15.
     * @version _._._
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public void bulkIndex(String url, String filepath) throws IOException, InterruptedException {

        String[] cmdarr = { "curl", "-X", "POST", url, "-H", "'Content-Type: application/x-ndjson'", "--data-binary", "@" + filepath };

        Process bulkProc = Runtime.getRuntime().exec(cmdarr);
        InputStream in = bulkProc.getInputStream();
        int r = -1;

        StringBuilder resultExec = new StringBuilder();
        while ((r = in.read()) != -1) {
            resultExec.append((char) r);
        }

        logger.trace("cmdarr={}", String.join(" ", cmdarr));
        logger.trace("exec  ={}", resultExec.toString());

        bulkProc.waitFor();

        int exitCode = bulkProc.exitValue();
        if (exitCode != 0) {
            logger.warn("Elasticsearch 'bulk' API 실행을 비정상적({})으로 종료되었습니다. command={}", exitCode, Arrays.toString(cmdarr));
        }
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
     * 데이터를 Elasticsearch Bulk JSON 형태로 변환하여 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 9. 15.		박준홍			최초 작성
     * </pre>
     *
     * @param <T>
     * @param data
     * @return
     *
     * @since 2022. 9. 15.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public <T> String createBulkJSONString(@NotNull List<T> data) {
        return data.parallelStream()
                // 단일 객체를 문자열로 변환
                .map(d -> RestApiUtils.toIndexBulkString(d))
                // 'null' 확인
                .filter(s -> s != null)
                // 하나의 문자열로 통합
                .collect(Collectors.joining());
    }

    /**
     * 데이터를 Elasticsearch JSON 문자열로 변환하여 저장한 후, 저장한 파일정보를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 9. 15.		박준홍			최초 작성
     * </pre>
     *
     * @param <T>
     *            데이터 타입
     * @param data
     *            데이터
     * @param tempfilePrefix
     *            임시파일 접두어
     * @param tempfileSuffix
     *            임시파일 접미어
     * @return 임시파일 절대 경로
     * @throws IOException
     *             파일 생성시 오류가 발생한 경우
     *
     * @since 2022. 9. 15.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public <T> String createBulkJSONTempFile(List<T> data, String tempfilePrefix, String tempfileSuffix) throws IOException {
        // #1. bulk JSON 문자열 생성
        String bulkJSONStr = createBulkJSONString(data);
        Reader reader = new BufferedReader(new StringReader(bulkJSONStr));

        // #2. bulk JSON 파일 생성
        File tmpfile = File.createTempFile(tempfilePrefix, tempfileSuffix);
        Writer writer = new FileWriter(tmpfile);

        IOUtils.transfer(reader, true, writer, true, IOUtils.BUFFER_SIZE_1MB);
        return tmpfile.getAbsolutePath();
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
