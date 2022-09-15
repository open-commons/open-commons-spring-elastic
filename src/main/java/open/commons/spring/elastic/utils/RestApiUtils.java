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
 * Date  : 2022. 9. 2. 오후 2:46:44
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

package open.commons.spring.elastic.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import open.commons.core.utils.AnnotationUtils;

/**
 * 
 * @since 2022. 9. 2.
 * @version 0.2.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 */
public class RestApiUtils {

    private static final BiFunction<Field, Object, Object> _value_ = (f, o) -> {
        boolean access = f.isAccessible();
        try {
            f.setAccessible(true);
            return f.get(o);
        } catch (IllegalArgumentException | IllegalAccessException ignored) {
            ignored.printStackTrace();
            return null;
        } finally {
            f.setAccessible(access);
        }
    };

    private RestApiUtils() {
    }

    /**
     * 객체의 필드값을 <code>"{이름}":값</code> 형태로 제공합니다.<br>
     * 지원 타입 (2022/09/01)
     * <ul>
     * <li>boolean
     * <li>byte
     * <li>short
     * <li>char
     * <li>int
     * <li>long
     * <li>float
     * <li>double
     * <li>{@link Boolean}
     * <li>{@link Byte}
     * <li>{@link Short}
     * <li>{@link Character}
     * <li>{@link Integer}
     * <li>{@link Long}
     * <li>{@link Float}
     * <li>{@link Double}
     * <li>{@link String}
     * <li>{@link AtomicBoolean}
     * <li>{@link AtomicInteger}
     * <li>{
     * 
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 9. 2.		박준홍			최초 작성
     * </pre>
     *
     * @param obj
     * @param f
     * @return
     *
     * @since 2022. 9. 2.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     */
    private static String readNavAsString(Object obj, Field f) {

        String nav = null;

        String name = String.join(f.getName(), "\"", "\"");
        // #2. 필드 값
        Class<?> clazz = f.getType();
        Object value = null;
        // #2-1. ture/false, number
        if (
        // primitive type
        clazz == boolean.class //
                || clazz == byte.class //
                || clazz == short.class //
                || clazz == int.class //
                || clazz == long.class //
                || clazz == float.class //
                || clazz == double.class //
                // wrapper type
                || clazz == Boolean.class //
                || clazz == Byte.class //
                || clazz == Short.class //
                || clazz == Integer.class //
                || clazz == Long.class //
                || clazz == Float.class //
                || clazz == Double.class //
        ) {
            value = _value_.apply(f, obj);
        }
        // #2-3. String, char, Character
        if (clazz == char.class //
                || clazz == Character.class //
                || clazz == String.class //
        ) {
            value = _value_.apply(f, obj);
            if (value != null) {
                // 큰 따옴표로 감싸기
                value = String.join(value.toString(), "\"", "\"");
            }
        }
        // #3-2. extended number type
        if (clazz == AtomicBoolean.class //
                || clazz == AtomicInteger.class //
                || clazz == AtomicLong.class //
        ) {
            value = _value_.apply(f, obj);
            if (value != null) {
                // {"value": ...} 형태
                value = String.join("", "{", "\"value\"", ":", value.toString(), "}");
            }
        }
        // #3-3. Date
        // TODO. 개발 예정

        if (value != null) {
            nav = String.join(":", name, value.toString());
        }

        return nav;
    }

    /**
     * 주어진 객체를 'bulk' API에 사용되는 JSON 문자열로 변환하여 제공합니다.<br>
     * <p>
     * 문자열 형태
     * 
     * <pre>
     * {header}
     * {body}
     * new_line
     * </pre>
     * </p>
     * 
     * 지원 타입 (2022/09/01)
     * <ul>
     * <li>boolean
     * <li>byte
     * <li>short
     * <li>char
     * <li>int
     * <li>long
     * <li>float
     * <li>double
     * <li>{@link Boolean}
     * <li>{@link Byte}
     * <li>{@link Short}
     * <li>{@link Character}
     * <li>{@link Integer}
     * <li>{@link Long}
     * <li>{@link Float}
     * <li>{@link Double}
     * <li>{@link String}
     * <li>{@link AtomicBoolean}
     * <li>{@link AtomicInteger}
     * <li>{@link AtomicLong}
     * </ul>
     * <br>
     * 
     * <pre>
     * [개정이력]
     *      날짜    	| 작성자	|	내용
     * ------------------------------------------
     * 2022. 9. 2.		박준홍			최초 작성
     * </pre>
     *
     * @param operation
     * @param obj
     * @return JSON 문자열. 단 데이터가 존재하지 않는 경우 <code>null</code>을 제공합니다.
     *
     * @since 2022. 9. 2.
     * @version 0.2.0
     * @author Park Jun-Hong (parkjunhong77@gmail.com)
     * 
     * @see #readNavAsString(Object, Field)
     */
    public static String toIndexBulkString(Object obj) {
        // #1. header
        String header = "{\"index\":{}}" + System.lineSeparator();

        // #2. 데이터 생성
        @SuppressWarnings("unchecked")
        List<Field> fields = AnnotationUtils.getAnnotatedFieldsAllHierarchy(obj, org.springframework.data.elasticsearch.annotations.Field.class);
        String data = fields.parallelStream()//
                .map(f -> readNavAsString(obj, f)) //
                .filter(s -> s != null) //
                .collect(Collectors.joining(","));

        if (data == null) {
            return null;
        }

        // #3. body
        String body = String.join("", header, "{", data, "}", System.lineSeparator());

        return body;
    }
}
