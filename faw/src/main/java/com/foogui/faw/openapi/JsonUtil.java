package com.foogui.faw.openapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Leon
 * @date 2022-08-18 14:07
 */
public class JsonUtil {

    public static final ObjectMapper CAMEL_CASE_OBJECT_MAPPER = new ObjectMapper();

    static {
        CAMEL_CASE_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 将json字符串转为java对象
     *
     * @param json json字符串
     * @param type 要转化的类型
     * @return java对象
     * @throws RuntimeException 转化异常
     */
    public static <T> T jsonToObject(String json, Class<T> type) {
        if (null == json) {
            return null;
        }

        try {
            return CAMEL_CASE_OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json字符串转为含有泛型的java对象
     * 例子: List<User> user = JsonUtils.fromJson("[{\"id\": \"01\", \"name\": \"hello world\"}]", new TypeReference<List<User>>() {});
     *
     * @param json          json字符串
     * @param typeReference 要转化的类型
     * @return java对象
     * @throws RuntimeException 转化异常
     */
    public static <T> T jsonToObject(String json, TypeReference<T> typeReference) {
        if (null == json) {
            return null;
        }

        try {
            return CAMEL_CASE_OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将java对象转为json字符串
     *
     * @param object java对象
     * @return json字符串
     * @throws RuntimeException 转化异常
     */
    public static <T> String toJsonString(T object) {
        if (null == object) {
            return null;
        }

        try {
            return CAMEL_CASE_OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
