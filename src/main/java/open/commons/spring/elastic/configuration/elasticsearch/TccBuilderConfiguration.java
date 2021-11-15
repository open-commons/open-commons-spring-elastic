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
 * Date  : 2021. 11. 15. 오후 2:58:40
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

package open.commons.spring.elastic.configuration.elasticsearch;

import java.time.Duration;

import org.springframework.data.elasticsearch.client.ClientConfiguration.TerminalClientConfigurationBuilder;
import org.springframework.http.HttpHeaders;

import open.commons.utils.StringUtils;

/**
 * 기본 클라이언트 설정 정보.
 * 
 * @since 2021. 11. 15.
 * @version 0.1.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 * @see {@link TerminalClientConfigurationBuilder}
 */
public class TccBuilderConfiguration {

    /** @see TerminalClientConfigurationBuilder#withBasicAuth(String, String)'s <b><code>username</code></b> */
    protected String username;
    /** @see TerminalClientConfigurationBuilder#withBasicAuth(String, String)'s <b><code>password</code></b> */
    protected String password;

    /** @see TerminalClientConfigurationBuilder#withConnectTimeout(Duration) */
    protected Duration connecTimeout;

    /** @see TerminalClientConfigurationBuilder#withDefaultHeaders(HttpHeaders) */
    protected HttpHeaders defaultHeaders;

    /** @see TerminalClientConfigurationBuilder#withPathPrefix(String) */
    protected String pathPrefix;

    /** @see TerminalClientConfigurationBuilder#withProxy(String) */
    protected String proxy;

    /** @see TerminalClientConfigurationBuilder#withSocketTimeout(Duration) */
    protected Duration socketTimeout;

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
    public TccBuilderConfiguration() {
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
     * @return the connecTimeout
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #connecTimeout
     */

    public Duration getConnecTimeout() {
        return connecTimeout;
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
     * @return the defaultHeaders
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #defaultHeaders
     */

    public HttpHeaders getDefaultHeaders() {
        return defaultHeaders;
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
     * @return the password
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #password
     */

    public String getPassword() {
        return password;
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
     * @return the pathPrefix
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #pathPrefix
     */

    public String getPathPrefix() {
        return pathPrefix;
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
     * @return the proxy
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #proxy
     */

    public String getProxy() {
        return proxy;
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
     * @return the socketTimeout
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #socketTimeout
     */

    public Duration getSocketTimeout() {
        return socketTimeout;
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
     * @return the username
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #username
     */

    public String getUsername() {
        return username;
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
     * @param connecTimeout
     *            the connecTimeout to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #connecTimeout
     */
    public void setConnecTimeout(Duration connecTimeout) {
        this.connecTimeout = connecTimeout;
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
     * @param defaultHeaders
     *            the defaultHeaders to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #defaultHeaders
     */
    public void setDefaultHeaders(HttpHeaders defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
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
     * @param password
     *            the password to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #password
     */
    public void setPassword(String password) {
        this.password = password;
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
     * @param pathPrefix
     *            the pathPrefix to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #pathPrefix
     */
    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
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
     * @param proxy
     *            the proxy to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #proxy
     */
    public void setProxy(String proxy) {
        this.proxy = proxy;
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
     * @param socketTimeout
     *            the socketTimeout to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #socketTimeout
     */
    public void setSocketTimeout(Duration socketTimeout) {
        this.socketTimeout = socketTimeout;
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
     * @param username
     *            the username to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TccBuilderConfiguration [username=");
        builder.append(username);
        builder.append(", password=");
        builder.append(password);
        builder.append(", connecTimeout=");
        builder.append(connecTimeout);
        builder.append(", defaultHeaders=");
        builder.append(defaultHeaders);
        builder.append(", pathPrefix=");
        builder.append(pathPrefix);
        builder.append(", proxy=");
        builder.append(proxy);
        builder.append(", socketTimeout=");
        builder.append(socketTimeout);
        builder.append("]");
        return builder.toString();
    }

    /**
     * 기본 인증정보를 설정 여부를 제공합니다. <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2021. 11. 15.		박준홍			최초 작성
     * </pre>
     *
     * @return
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     * 
     * @see #setUsername(String)
     * @see #setPassword(String)
     * @see TerminalClientConfigurationBuilder#withBasicAuth(String, String)
     */
    public boolean usableBasicAuth() {
        return !StringUtils.isNullOrEmptyStringOr(this.username, this.password);
    }
}
