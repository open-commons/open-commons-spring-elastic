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

