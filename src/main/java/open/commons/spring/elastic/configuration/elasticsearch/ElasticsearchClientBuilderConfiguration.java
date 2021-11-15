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
 * Date  : 2021. 11. 15. 오후 3:24:00
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

package open.commons.spring.elastic.configuration.elasticsearch;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Elasticsearch 클라이언트 설정 정보.
 * 
 * @since 2021. 11. 15.
 * @version 0.1.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 */
public abstract class ElasticsearchClientBuilderConfiguration<E extends EndpointBuilderConfiguration, S extends SccBuilderConfiguration, T extends TccBuilderConfiguration>
        implements InitializingBean {

    protected E endpoint;
    protected S secure;
    protected T terminal;

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
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    public ElasticsearchClientBuilderConfiguration() {
    }

    /**
     *
     * @since 2021. 11. 15.
     * @version _._._
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.endpoint, "Endpoint 정보는 반드시 설정되어야 합니다.");
        Assert.notNull(this.terminal, "Terminal Client 정보는 반드시 설정되어야 합니다.");
    }

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
     * @return the endpoint
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #endpoint
     */

    public E getEndpoint() {
        return endpoint;
    }

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
     * @return the secure
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #secure
     */

    public S getSecure() {
        return secure;
    }

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
     * @return the terminal
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #terminal
     */

    public T getTerminal() {
        return terminal;
    }

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
     * @param endpoint
     *            the endpoint to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #endpoint
     */
    public abstract void setEndpoint(E endpoint);

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
     * @param secure
     *            the secure to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #secure
     */
    public abstract void setSecure(S secure);

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
     * @param terminal
     *            the terminal to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #terminal
     */
    public abstract void setTerminal(T terminal);

}
