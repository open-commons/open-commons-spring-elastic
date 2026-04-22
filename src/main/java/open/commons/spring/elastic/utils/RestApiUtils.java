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
 * Date  : 2022. 9. 2. 오후 2:46:44
 *
 * Author: Park Jun-Hong (parkjunhong77@gmail.com)
 * 
 */

package open.commons.spring.elastic.utils;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import open.commons.core.utils.AnnotationUtils;

/**
 * 
 * @since 2022. 9. 2.
 * @version 0.2.0
 * @author Park Jun-Hong (parkjunhong77@gmail.com)
 */
public class RestApiUtils {

    private static final Logger logger = LoggerFactory.getLogger(RestApiUtils.class);

    @SuppressWarnings("unused")
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
            .withZone(ZoneId.systemDefault());

    private static final BiFunction<Field, Object, Object> _value_ = (f, o) -> {
        try {
            // 1. 현재 객체(o)에 대해 해당 필드(f)가 접근 가능한지 확인 (isAccessible 대체)
            if (!f.canAccess(o)) {
                // 2. 접근 불가 시 안전하게 권한 부여 시도 (InaccessibleObjectException 방지)
                if (!f.trySetAccessible()) {
                    // 모듈 시스템에 의해 강력히 캡슐화되어 접근할 수 없는 경우
                    // 필요에 따라 로깅 후 null을 반환하거나 예외를 던집니다.
                    return null;
                }
            }

            // 3. 값 반환
            return f.get(o);

        } catch (IllegalArgumentException | IllegalAccessException e) {
            logger.error("", e);
            return null;
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
     * 2022. 9. 2.		parkjunhong77@gmail.com			최초 작성
     * </pre>
     *
     * @param obj
     * @param f
     * @return
     *
     * @since 2022. 9. 2.
     * @version 0.2.0
     */
    /**
     * 객체의 필드값을 <code>"{이름}":값</code> 형태로 제공합니다. <br>
     * [모던 자바 리팩토링 적용 - JDK 25]
     */
    private static String readNavAsString(Object obj, Field f) {

        // 1. 값 먼저 추출 (이전 답변의 최적화된 _value_ 함수 사용)
        Object rawValue = _value_.apply(f, obj);

        // 값이 없으면 직렬화 생략
        if (rawValue == null) {
            return null;
        }

        // 2. 키(Key) 포맷팅: "fieldName"
        // (기존의 복잡한 String.join 대신 직관적인 결합 사용)
        String namePart = "\"" + f.getName() + "\"";

        // 3. 값(Value) 포맷팅: Java 21+ Pattern Matching for switch 적용
        String valuePart = switch (rawValue) {

            // #3-1. 확장 숫자형 (Number보다 먼저 매칭되어야 함)
            case AtomicBoolean ab -> "{\"value\":" + ab.get() + "}";
            case AtomicInteger ai -> "{\"value\":" + ai.get() + "}";
            case AtomicLong al -> "{\"value\":" + al.get() + "}";

            // #3-2. 일반 숫자 및 논리형 (기본형 int, double 등은 자동 Boxing 되어 여기서 매칭됨)
            case Boolean b -> b.toString();
            case Number n -> n.toString(); // Byte, Short, Integer, Long, Float, Double 통합 처리

            // #3-3. 문자 및 문자열 (따옴표 래핑)
            case Character c -> "\"" + c + "\"";
            case CharSequence s -> "\"" + s + "\""; // String을 포함한 모든 문자열 처리

            // #3-4. 레거시 Date (java.util.Date, java.sql.Timestamp 등)
            // -> toInstant()를 호출하면 "2026-04-22T11:27:10.123Z" 형태의 ISO-8601(UTC) 문자열을 반환합니다.
            case java.util.Date d -> "\"" + d.toInstant().toString() + "\"";

            // #3-5. 모던 Date/Time (LocalDate, LocalDateTime, ZonedDateTime, Instant 등)
            // -> 이 객체들은 TemporalAccessor를 구현하며, 고유의 toString() 자체가 완벽한 ISO-8601 규격입니다.
            case TemporalAccessor t -> "\"" + t.toString() + "\"";

            // // 🎯 [날짜 처리 - 포맷 강제 방식]
            // case java.util.Date d -> "\"" + ISO_FORMATTER.format(d.toInstant()) + "\"";
            // case TemporalAccessor t -> "\"" + ISO_FORMATTER.format(t) + "\"";

            default -> null;
        };

        // 4. 최종 JSON Key-Value 문자열 조립
        return (valuePart != null) ? namePart + ":" + valuePart : null;
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
     * 2022. 9. 2.		parkjunhong77@gmail.com     최초 작성
     * 2026. 4. 22.     parkjunohng77@gmail.com     JDK25 현행화
     * </pre>
     *
     * @param operation
     * @param obj
     * @return JSON 문자열. 단 데이터가 존재하지 않는 경우 <code>null</code>을 제공합니다.
     *
     * @since 2022. 9. 2.
     * @version 4.0.0
     * 
     * @see #readNavAsString(Object, Field)
     */
    public static String toNDJsonString(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }

        // #1. 대상 필드 추출
        List<Field> fields = AnnotationUtils.getAnnotatedFieldsAllHierarchy(obj,
                org.springframework.data.elasticsearch.annotations.Field.class);

        // #2. 데이터 생성 (순차 스트림 적용 및 메서드 레퍼런스 활용)
        String data = fields.stream() //
                .map(f -> readNavAsString(obj, f)).filter(Objects::nonNull) //
                .collect(Collectors.joining(","));

        if (data.isEmpty()) {
            return null;
        }

        // #3. NDJSON 포맷 조립 (헤더 + 바디)
        // Elasticsearch의 NDJSON(Newline Delimited JSON) 명세에 따라 '\n'으로 종료함.
        return "{\"index\":{}}\n" + "{" + data + "}\n";
    }
}
