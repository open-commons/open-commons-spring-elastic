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
 * Date  : 2021. 11. 15. 오후 2:06:22
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

package open.commons.spring.elastic.configuration.elasticsearch;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.data.elasticsearch.client.ClientConfiguration.ClientConfigurationBuilderWithRequiredEndpoint;

import open.commons.core.utils.NetUtils;

/**
 * Elasticsearch 연결 정보.
 * 
 * @since 2021. 11. 15.
 * @version 0.1.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 * @see {@link ClientConfigurationBuilderWithRequiredEndpoint}
 */
public class EndpointBuilderConfiguration {

    /**
     * Elasticsearch 서버 접속 정보.<br>
     * Format: {host}:{port}
     * <ul>
     * <li>192.168.0.1:80
     * <li>www.google.com:90
     * <li>::1:80
     * </ul>
     * 
     * 데이터
     * <ul>
     * <li>host: <code>Group 1</code>
     * <li>port: <code>Group 11</code>
     * </ul>
     */
    private static final Pattern PATTERN_CONNECTIONS = Pattern.compile(//
            "^(" //
                    + NetUtils.REGEX_IPV4 //
                    + "|" + NetUtils.REGEX_IPV6 //
                    + "|" + NetUtils.REGEX_DOMAIN //
                    + "):" //
                    + NetUtils.REGEX_PORT //
                    + "$" //
            , Pattern.CASE_INSENSITIVE);

    /**
     * Elasticsearch 서버 접속 정보<br>
     * <ul>
     * <li>포맷: <host>:<port>
     * <li>예: 192.168.0.12:9200
     * </ul>
     * 
     */
    private Set<String> connections;

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
    public EndpointBuilderConfiguration() {
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
     * @return the connections
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #connections
     */

    public Set<String> getConnections() {
        return connections;
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
     * @param connections
     *            the connections to set
     *
     * @since 2021. 11. 15.
     * @version 0.1.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     *
     * @see #connections
     */
    public void setConnections(Set<String> connections) {
        ArrayList<String> errMsg = new ArrayList<>();
        for (String conn : connections) {
            if (!PATTERN_CONNECTIONS.matcher(conn).matches()) {
                errMsg.add(conn);
            }
        }

        if (errMsg.size() > 0) {
            throw new IllegalArgumentException(String.format("올바르지 않은 접속정보 입니다. 대상=%s", errMsg));
        }

        this.connections = connections;
    }
}
