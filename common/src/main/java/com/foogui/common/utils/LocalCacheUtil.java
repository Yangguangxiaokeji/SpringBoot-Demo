package com.foogui.common.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 基于guava的本地缓存,含过期时间
 *
 * @author Foogui
 * @date 2023/07/28
 */
public class LocalCacheUtil {

    private final static Cache<String, Map<String, Object>> CACHE_MAP = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS).build();

    private final static Cache<String, String> CACHE_STRING = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS).build();


    public static Map<String, Object> getCacheMap(String key) {
        return CACHE_MAP.getIfPresent(key);
    }

    public static void setCacheMap(String key, Map<String, Object> map) {
        CACHE_MAP.put(key, map);
    }

    public static String getCacheString(String key) {
        return CACHE_STRING.getIfPresent(key);
    }

    public static void setCacheString(String key, String value) {
        CACHE_STRING.put(key, value);
    }



}
