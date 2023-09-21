package com.foogui.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotNull;
import java.io.IOException;

public class JsonUtils {
    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        // 设置反序列化时忽略掉 JSON 中的未知属性
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.findAndRegisterModules();
    }

    private JsonUtils() {
    }

    public static <T> T toObject(@NotNull String json, Class<T> objectType) {
        try {
            return OBJECT_MAPPER.readValue(json, objectType);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T toObject(@NotNull String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> String toJson(@NotNull T object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


}
