[2025/02/17]
- Snapshot: 0.4.0-SNAPSHOT
- Dependencies:
  + open-commons-spring-web-dependencies: 0.8.0-SNAPSHOT

[2025/02/17]
- Release: 0.3.0

[2024/10/31]
- ETC
  + Maven Repository 주소 변경 (http -> https)
  
[2024/04/11]
- New 
  + open.commons.spring.elastic.service.AbstractElasticClientService
- Delete
  + open.commons.spring.elastic.service.AbstractElasticsearchService
    + createElasticsearchAsyncClient(RestClient): open.commons.spring.elastic.service.AbstractElasticClientService로 이관
    + createElasticsearchClient(RestClient): open.commons.spring.elastic.service.AbstractElasticClientService로 이관
    + createIndex(String, String): open.commons.spring.elastic.service.AbstractElasticClientService로 이관
  
[2023/10/20]
- Modify
  + open.commons.spring.elastic.service.AbstractElasticsearchService
    + createElasticsearchAsyncClient(RestClient): 비동기 클라이언트 지원
  + open.commons.spring.elastic.utils.RestClients
    + createElasticsearchAsyncClient(ElasticsearchTransport): 비동기 클라이언트 지원
    + createElasticsearchAsyncClient(ElasticsearchTransport, TransportOptions): 비동기 클라이언트 지원
    + createElasticsearchAsyncClient(RestClient, JsonpMapper): 비동기 클라이언트 지원
    + createElasticsearchAsyncClient(RestClient, JsonpMapper, TransportOptions): 비동기 클라이언트 지원

[2023/10/13]
- Dependencies
  + spring-core: 5.3.30
  + spring-boot: 2.7.16
- Migraiton from the High Level Rest Client to Java API Client  
  + Add
    + open.commons.spring.elastic.utils.RestClients: org.springframework.data.elasticsearch.client.RestClients 를 기본으로 Java API Client에 맞도록 코드 수정 및 기능 추가
  + Modify
    + open.commons.spring.elastic.service.AbstractElasticsearchService: Java API Client 대응 수정

[2023/09/07]
- Dependencies
  + open-commons-spring-web: 0.7.0-SNAPSHOT
  
[2023/09/05]
- Release: 0.3.0-SNAPSHOT
- Dependencies
  + spring-core:: 5.3.29
  + spring-boot: 2.7.15
  + sl4jf-api: 2.0.9
  + open-commons-spring-web: 0.6.0-SNAPSHOT

[2023/09/05]
- Release: 0.2.0

[2022/11/17]
- Dependencies
	+ spring-core.version: 5.3.23 고정
	
[2022/10/13]
- Add
  + open.commons.spring.elastic.service.AbstractElasticsearchService
    + createBulkIndexAction(List&lt;T&gt;, BiFunction&lt;ElasticsearchRestTemplate, List&lt;IndexQuery&gt;, List&lt;IndexedObjectInformation&gt;&gt;)  
    + createBulkIndexAction(List&lt;T&gt;, String)
    + createIndex(String, String): 주어진 이름과 매핑 정보를 이용하여 Index 를 생성

[2022/09/15]
- Add
  + open.commons.spring.elastic.service.AbstractElasticsearchService
    + bulkIndex(String, String)
    + createBulkJSONString(List&lt;T&gt;)
    + createBulkJSONTempFile(List&lt;T&gt;, String, String)

[2022/09/02]
- New
  + open.commons.spring.elastic.utils.RestApiUtils

[2022/05/17]
- Add
  + open.commons.spring.elastic.service.AbstractElasticsearchService

[2022/04/07]
- Release: 0.2.0-SNAPSHOT
- Tag: 0.1.0
- Dependencies
  + open.commons.core: 2.0.0-SNAPSHOT

[2022/04/07]
- Release: 0.1.0

[2021/11/24]
- Bugfix
  + open.commons.spring.elastic.configuration.elasticsearch.TccBuilderConfiguration: 속성 오타 수정
    + connecTimeout -> connectTimeout

[2021/11/15]
- Modify
  + open.commons.spring.elastic.configurations.elasticsearch.ElasticsearchClientConfiguration
    + InitializingBean 구현 추가
- New
  + open.commons.spring.elastic.utils.ConfigurationUtils
- Modify
  + open.commons.spring.elastic.configurations.elasticsearch
    + EndpointBuilderConfiguration.java
    + SccBuilderConfiguration.java
    + TccBuilderConfiguration.java  
- New
  + open.commons.spring.elastic.configurations.elasticsearch
    + DefaultClientConfiguration.java
    + ElasticsearchClientConfiguration.java
    + <strike>EndpointConfiguration.java</strike>
    + <strike>SecureClientConfiguration.java</strike>
    + <strike>TerminalClientConfiguration.java</strike>

