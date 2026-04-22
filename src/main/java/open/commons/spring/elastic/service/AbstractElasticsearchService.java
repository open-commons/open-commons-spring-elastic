/*
 * Copyright 2022 Park Jun-Hong (parkjunhong77@gmail.com)
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;

import org.jspecify.annotations.Nullable;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery.OpType;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import open.commons.core.Result;
import open.commons.core.utils.AssertUtils2;
import open.commons.core.utils.IOUtils;
import open.commons.spring.elastic.utils.RestApiUtils;

/**
 * * <br>
 * *
 * 
 * <pre>
 * [개정이력]
 * 날짜       | 작성자   |   내용
 * ------------------------------------------
 * 2022. 5. 17.         parkjunhong77@gmail.com     최초 작성
 * 2023. 10. 13.        parkjunhong77@gmail.com     Migrate from the High Level Rest Client to Java API Client.
 * 2024. 4. 11.         parkjunhong77@gmail.com     Java API Client 기능을 {@link AbstractElasticClientService}로 이관시킴.
 * 2026. 4. 22.     parkjunhong77@gmail.com     Spring Data Elasticsearch 6.0.3 및 Java 25 환경으로 마이그레이션.
 * </pre>
 * 
 * * @since 2022. 5. 17.
 * 
 * @version 4.0.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 */
public abstract class AbstractElasticsearchService extends AbstractElasticClientService {

    protected final ClientConfiguration esClientConfig;
    protected final ElasticsearchConverter esConverter;

    /**
     * <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 5. 17.     parkjunhong77@gmail.com     최초 작성
     * </pre>
     * 
     * * @param esClientConfig Elasticsearch 클라이언트 설정
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     */
    public AbstractElasticsearchService(ClientConfiguration esClientConfig) {
        this(esClientConfig, null);
    }

    /**
     * * <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2023. 10. 13.        parkjunhong77@gmail.com     최초 작성
     * </pre>
     *
     * @param esClientConfig
     *            Elasticsearch 클라이언트 설정
     * @param esConverter
     *
     * @since 2023. 10. 13.
     * @version 0.3.0
     */
    public AbstractElasticsearchService(ClientConfiguration esClientConfig,
            @Nullable ElasticsearchConverter esConverter) {
        super(esClientConfig);
        this.esClientConfig = esClientConfig;
        this.esConverter = esConverter != null //
                ? esConverter //
                : createElasticsearchConverter();
    }

    /**
     * Elasticsearch JSON 으로 저장된 파일을 한번에 저장합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 9. 15.     parkjunhong77@gmail.com     최초 작성
     * parkjunhong77@gmail.com     최초 작성
     * 2026. 4. 22.     parkjunhong77@gmail.com         프로세스 스트림을 Java 9+ 방식으로 안전하게 비우도록 개선
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
     * @version 4.0.0
     */
    public void bulkIndex(@NotBlank String url, @NotBlank String filepath) throws IOException, InterruptedException {
        AssertUtils2.notBlanks(url, filepath);

        String[] cmdarr = { "curl", "-X", "POST", url, "-H", "Content-Type: application/x-ndjson", "--data-binary",
                "@" + filepath };

        Process bulkProc = Runtime.getRuntime().exec(cmdarr);

        try (InputStream in = bulkProc.getInputStream()) {
            in.transferTo(OutputStream.nullOutputStream());
        }

        bulkProc.waitFor();

        int exitCode = bulkProc.exitValue();
        if (exitCode != 0) {
            logger.warn("Elasticsearch 'bulk' API 실행이 비정상적({})으로 종료되었습니다. command={}", exitCode,
                    Arrays.toString(cmdarr));
        }
    }

    /**
     * 데이터를 Elasticsearch 작업 데이터로 변환하여 제공합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 5. 17.     parkjunhong77@gmail.com     최초 작성
     * 2026. 4. 22.     parkjunhong77@gmail.com     Stream.toList() 적용
     * </pre>
     *
     * @param <T>
     * @param data
     * @return
     *
     * @since 2022. 5. 17.
     * @version 4.0.0
     */
    public <T> List<IndexQuery> createBulk(Collection<T> data) {
        AssertUtils2.notNull(data);

        return data.parallelStream().map(d -> new IndexQueryBuilder() //
                .withObject(d)//
                .withOpType(OpType.CREATE) //
                .build()).toList(); // 🎯 Java 16+ 불변 리스트 반환
    }

    /**
     * 주어진 데이터를 'bulk index'를 이용하여 추가합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 10. 17.    parkjunhong77@gmail.com     최초 작성
     * 2023. 10. 13.    parkjunhong77@gmail.com     Migrate from the High Level Rest Client to Java API Client.
     * </pre>
     *
     * @param <T>
     * @param data
     *            데이터
     * @param bulkIndexFx
     *            실행 함수
     * @return
     *
     * @since 2022. 10. 17.
     * @version 0.2.0
     */
    public <T> Supplier<Result<List<IndexedObjectInformation>>> createBulkIndexAction(List<T> data //
            , BiFunction<ElasticsearchOperations, List<IndexQuery>, List<IndexedObjectInformation>> bulkIndexFx) {
        AssertUtils2.notNulls(data, bulkIndexFx);

        return () -> {
            try {
                ElasticsearchOperations esOp = getElasticsearchOperations();
                List<IndexQuery> queries = createBulk(data);
                return Result.success(bulkIndexFx.apply(esOp, queries));
            } catch (Exception e) {
                return Result.error(e.getMessage());
            }
        };
    }

    /**
     * 다수 개의 데이터를 생성하는 작업을 제공합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 5. 17.     parkjunhong77@gmail.com     최초 작성
     * </pre>
     *
     * @param <T>
     * @param data
     *            데이터
     * @param type
     *            IndexEntity class
     * @return
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     */
    public <T> Supplier<Result<List<IndexedObjectInformation>>> createBulkIndexAction(List<T> data, Class<T> type) {
        AssertUtils2.notNulls(data, type);

        return createBulkIndexAction(data, (esOp, queries) -> esOp.bulkIndex(queries, type));
    }

    /**
     * 다수 개의 데이터를 생성하는 작업을 제공합니다. <br>
     * * <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 10. 13.        parkjunhong77@gmail.com     최초 작성
     * </pre>
     *
     * @param <T>
     * @param data
     *            데이터
     * @param indexName
     *            'index' 이름
     * @return
     *
     * @since 2022. 10. 13.
     * @version 0.2.0
     */
    public <T> Supplier<Result<List<IndexedObjectInformation>>> createBulkIndexAction(List<T> data,
            @NotBlank String indexName) {
        AssertUtils2.notNull(data);
        AssertUtils2.notBlank(indexName);

        return createBulkIndexAction(data, (esOp, queries) -> esOp.bulkIndex(queries, IndexCoordinates.of(indexName)));
    }

    /**
     * 데이터를 Elasticsearch JSON 문자열로 변환하여 저장한 후, 저장한 파일정보를 제공합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 9. 15.     parkjunhong77@gmail.com     최초 작성
     * 2026. 4. 22.     parkjunhong77@gmail.com         try-with-resources 블록을 사용하여 안전한 자원 반납
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
     * @version 4.0.0
     */
    public <T> String createBulkJSONTempFile(List<T> data, @NotBlank String tempfilePrefix,
            @NotBlank String tempfileSuffix) throws IOException {
        AssertUtils2.notNull(data);
        AssertUtils2.notBlanks(tempfilePrefix, tempfileSuffix);

        // #1. bulk JSON 문자열 생성
        String bulkNDJsonStr = createNDJsonString(data);

        // #2. bulk JSON 파일 생성
        File tmpfile = File.createTempFile(tempfilePrefix, tempfileSuffix);

        // 🎯 try-with-resources를 통해 Reader/Writer 안전 반납
        try (Reader reader = new StringReader(bulkNDJsonStr); Writer writer = new FileWriter(tmpfile)) {
            IOUtils.transfer(reader, writer);
        }

        return tmpfile.getAbsolutePath();
    }

    private ElasticsearchConverter createElasticsearchConverter() {
        MappingElasticsearchConverter mappingElasticsearchConverter = new MappingElasticsearchConverter(
                new SimpleElasticsearchMappingContext());
        mappingElasticsearchConverter.afterPropertiesSet();
        return mappingElasticsearchConverter;
    }

    /**
     * 데이터를 Elasticsearch Bulk JSON 형태로 변환하여 제공합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 9. 15.     parkjunhong77@gmail.com     최초 작성
     * 2026. 4. 22.     parkjunhong77@gmail.com         메서드 레퍼런스 적용
     * </pre>
     *
     * @param <T>
     * @param data
     * @return
     *
     * @since 2022. 9. 15.
     * @version 4.0.0
     */
    public <T> String createNDJsonString(List<T> data) {
        AssertUtils2.notNull(data);

        return data.parallelStream()
                // 단일 객체를 문자열로 변환
                .map(RestApiUtils::toNDJsonString)
                // 'null' 확인
                .filter(Objects::nonNull)
                // 하나의 문자열로 통합
                .collect(Collectors.joining());
    }

    /**
     * 데이터 삭제하고 결과를 제공합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 5. 17.     parkjunhong77@gmail.com     최초 작성
     * 2023. 10. 13.    parkjunhong77@gmail.com     Migrate from the High Level Rest Client to Java API Client.
     * 2026. 4. 22.     parkjunhong77@gmail.com     SDE 6.x 명세에 맞춰 파라미터를 Query에서 DeleteQuery로 변경
     * </pre>
     *
     * @param query
     *            삭제 전용 쿼리 (DeleteQuery)
     * @param clazz
     *            대상 클래스 타입
     * @return ByQueryResponse 삭제 결과
     *
     * @since 2022. 5. 17.
     * @version 4.0.0
     */
    public ByQueryResponse delete(DeleteQuery query, Class<?> clazz) {
        AssertUtils2.notNulls(query, clazz);

        ElasticsearchOperations esOp = getElasticsearchOperations();
        return esOp.delete(query, clazz);
    }

    /**
     * {@link co.elastic.clients.elasticsearch.ElasticsearchClient}를 이용하여 생성된
     * {@link ElasticsearchTemplate} 연동 객체를 제공합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 5. 17.     parkjunhong77@gmail.com     최초 작성
     * 2023. 10. 13.    parkjunhong77@gmail.com     Migrate from the High Level Rest Client to Java API Client.
     * 2026. 4. 22.     parkjunhong77@gmail.com     SDE 6.0의 ElasticsearchTemplate 생성자 호환성 확인
     * </pre>
     *
     * @return
     *
     * @since 2022. 5. 17.
     * @version 4.0.0
     */
    public ElasticsearchOperations getElasticsearchOperations() {
        return new ElasticsearchTemplate(this.esClient, this.esConverter);
    }

    /**
     * 검색된 데이터만 제공합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 5. 17.     parkjunhong77@gmail.com     최초 작성
     * 2026. 4. 22.     parkjunhong77@gmail.com     Stream.toList() 및 메서드 레퍼런스 적용
     * </pre>
     *
     * @param <E>
     * @param query
     * @param type
     * @return
     *
     * @since 2022. 5. 17.
     * @version 4.0.0
     */
    public <E> List<E> search(Query query, Class<E> type) {
        AssertUtils2.notNulls(query, type);

        SearchHits<E> searchHits = searchHits(query, type);
        return searchHits.stream() //
                .map(SearchHit::getContent) //
                .toList();
    }

    /**
     * 데이터를 검색하고 결과를 제공합니다. <br>
     * *
     * 
     * <pre>
     * [개정이력]
     *     날짜        | 작성자                   |   내용
     * -----------------------------------------------------
     * 2022. 5. 17.     parkjunhong77@gmail.com     최초 작성
     * </pre>
     *
     * @param <E>
     * @param query
     * @param type
     * @return
     *
     * @since 2022. 5. 17.
     * @version 0.2.0
     */
    public <E> SearchHits<E> searchHits(Query query, Class<E> type) {
        AssertUtils2.notNulls(query, type);

        ElasticsearchOperations esOp = getElasticsearchOperations();
        return esOp.search(query, type);
    }

}