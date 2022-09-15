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

