/*
 * Copyright 2021 Park Jun-Hong (parkjunhong77@gmail.com)
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
 * Date  : 2021. 11. 15. 오후 3:32:12
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

package open.commons.spring.elastic.configuration.elasticsearch;

/**
 * 
 * @since 2021. 11. 15.
 * @version 0.1.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 */
public class DefaultClientBuilderConfiguration extends ElasticsearchClientBuilderConfiguration<EndpointBuilderConfiguration, SccBuilderConfiguration, TccBuilderConfiguration> {

    /**
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2021. 11. 15.		박준홍			최초 작성
     * </pre>
     *
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public DefaultClientBuilderConfiguration() {
    }

    /**
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see open.commons.spring.elastic.configuration.elasticsearch.ElasticsearchClientBuilderConfiguration#setEndpoint(open.commons.spring.elastic.configuration.elasticsearch.EndpointBuilderConfiguration)
     */
    @Override
    public void setEndpoint(EndpointBuilderConfiguration endpoint) {
        this.endpoint = endpoint;
    }

    /**
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see open.commons.spring.elastic.configuration.elasticsearch.ElasticsearchClientBuilderConfiguration#setSecure(open.commons.spring.elastic.configuration.elasticsearch.SccBuilderConfiguration)
     */
    @Override
    public void setSecure(SccBuilderConfiguration secure) {
        this.secure = secure;
    }

    /**
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see open.commons.spring.elastic.configuration.elasticsearch.ElasticsearchClientBuilderConfiguration#setTerminal(open.commons.spring.elastic.configuration.elasticsearch.TccBuilderConfiguration)
     */
    @Override
    public void setTerminal(TccBuilderConfiguration terminal) {
        this.terminal = terminal;
    }

}
