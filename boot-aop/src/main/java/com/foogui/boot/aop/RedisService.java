package com.foogui.boot.aop;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


@Slf4j
@Service
public class RedisService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // =============================拆箱util============================
    private boolean unbox(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    private long unbox(Long value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    // =============================common============================

    /**
     * 对key设置缓存时间
     *
     * @param key      键
     * @param duration 时间（秒）
     * @return boolean
     */
    public boolean expire(@Nonnull String key, long duration) {
        return unbox(redisTemplate.expire(key, duration, TimeUnit.SECONDS));
    }

    /**
     * 获取key的过期时间
     *
     * @param key 键
     * @return long 剩余过期时间（秒）
     */
    public long getExpire(@Nonnull String key) {
        return unbox(redisTemplate.getExpire(key, TimeUnit.SECONDS));
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true存在 ，false不存在
     */
    public boolean hasKey(@Nonnull String key) {
        return unbox(redisTemplate.hasKey(key));
    }


    /**
     * 删除缓存
     *
     * @param key 键
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            redisTemplate.delete(Arrays.asList(key));
        }
    }

    /**
     * 值递增
     * value必须是整数类型或数字字符串，例如 1 或 "1"都可行
     *
     * @param key   键
     * @param delta 递增步长
     * @return 最新的value值
     */
    public long incr(@Nonnull String key, long delta) {
        return unbox(redisTemplate.opsForValue().increment(key, delta));
    }

    /**
     * 值递减
     * value必须是整数类型或数字字符串，例如 1 或 "1"都可行
     *
     * @param key   键
     * @param delta 减少数量
     * @return 减少数量
     */
    public long decr(@Nonnull String key, long delta) {
        return unbox(redisTemplate.opsForValue().decrement(key, delta));
    }

    // ============================缓存值为String=============================

    /**
     * 根据key获取缓存值
     *
     * @param key 键
     * @return {@link Object} key不存在返回null
     */
    public String getString(@Nonnull String key) {
        Object object = getObject(key);
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String) object;
        }
        throw new IllegalStateException("the type of value in redis is " + object.getClass() + ", you shouldn't user getString");
    }

    /**
     * 根据key获取缓存值并转为clazz类型，
     * 要求缓存value为String类型
     *
     * @param key   键
     * @param clazz 对象类型
     * @return {@link T}
     */
    public <T> T getStringToObject(@Nonnull String key, Class<T> clazz) {
        String json = getString(key);
        if (json == null) {
            return null;
        } else {
            try {
                return OBJECT_MAPPER.readValue(json, clazz);
            } catch (Exception e) {
                throw new IllegalStateException("the type of value in redis is not" + clazz);
            }
        }
    }


    /**
     * 存缓存值
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setString(@Nonnull String key, @Nonnull String value) {
        return setObject(key, value);
    }

    /**
     * 缓存并设置过期时间
     *
     * @param key      键
     * @param value    值
     * @param duration 时间(秒) time ≤ 0 表示无限期
     * @return true成功 false 失败
     */
    public boolean setString(@Nonnull String key, @Nonnull String value, long duration) {
        return setObject(key, value, duration);
    }


    // ============================缓存值为对象类型Object=============================

    /**
     * 缓存并设置过期时间
     *
     * @param key      键
     * @param value    值
     * @param duration 时间(秒) time ≤ 0 表示无限期
     * @return true成功 false 失败
     */
    public boolean setObject(@Nonnull String key, @Nonnull Object value, long duration) {
        try {
            redisTemplate.opsForValue().set(key, value, duration, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setObject(@Nonnull String key, @Nonnull Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 根据 key获取缓存值
     *
     * @param key 键
     * @return {@link Object} key不存在返回null
     */
    public Object getObject(@Nonnull String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // ================================值为Map=================================

    /**
     * 获取hashKey对应的值
     *
     * @param key     键
     * @param hashKey map的key
     * @return 值
     */
    public Object hGet(@Nonnull String key, @Nonnull String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多Map
     */
    public Map<Object, Object> hGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取对应key的值集合
     *
     * @param key      键
     * @param hashKeys Map中的keys
     * @return {@link List}<{@link Object}>
     */
    public List<Object> hGet(String key, String... hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, Arrays.asList(hashKeys));
    }

    /**
     * 获得key对应的Map的key集合
     *
     * @param key 键
     * @return {@link Set}<{@link Object}>
     */
    public Set<Object> hGetKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获得key对应的Map的value集合
     *
     * @param key 键
     * @return {@link List}<{@link Object}>
     */
    public List<Object> hGetValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 存Map
     *
     * @param key 键
     * @param map Map
     * @return true 成功
     */
    public boolean hSetMap(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 存Map，设置过期时间
     *
     * @param key      键
     * @param map      Map，key为String
     * @param duration 时间(秒)
     * @return true成功
     */
    public boolean hSetMap(String key, Map<String, Object> map, long duration) {
        try {
            if (hSetMap(key, map) && duration > 0) {
                return expire(key, duration);
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 向Map中放入数据,如果不存在将创建
     *
     * @param key     键
     * @param hashKey Map的key
     * @param value   值
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 向Map中放入数据,如果不存在将创建,并设置过期时间
     *
     * @param key      键
     * @param hashKey  Map的key
     * @param value    值
     * @param duration 时间(秒)
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String hashKey, Object value, long duration) {
        try {
            if (hSet(key, hashKey, value) && duration > 0) {
                return expire(key, duration);
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 删除Map中的k-v
     *
     * @param key      键
     * @param hashKeys Map中要删除的keys
     */
    public void hDelete(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 判断Map中是否有该key
     *
     * @param key     键
     * @param hashKey Map中的key
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * Map中key对应的value递增
     *
     * @param key     键
     * @param hashKey Map中的key
     * @param delta   步长(大于0)
     * @return 最新值
     */
    public long hIncr(String key, String hashKey, long delta) {
        return unbox(redisTemplate.opsForHash().increment(key, hashKey, delta));
    }

    /**
     * Map中key对应的value递减
     *
     * @param key     键
     * @param hashKey Map中的key
     * @param delta   步长(小于0)
     * @return 最新值
     */
    public long hDecr(String key, String hashKey, long delta) {
        return unbox(redisTemplate.opsForHash().increment(key, hashKey, delta));
    }

    // ============================值为Set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return Set中的所有值
     */

    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(key, e);
            return Collections.emptySet();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return unbox(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return unbox(redisTemplate.opsForSet().add(key, values));
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key      键
     * @param duration 时间(秒)
     * @param values   值
     * @return 成功个数
     */
    public long sSetWithTime(String key, long duration, Object... values) {
        try {
            long count = sSet(key, duration);
            if (duration > 0) {
                expire(key, duration);
            }
            return count;
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return set缓存的长度
     */
    public long sGetSize(String key) {
        try {
            return unbox(redisTemplate.opsForSet().size(key));
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 移除值
     *
     * @param key    键
     * @param values 值
     * @return 移除的个数
     */
    public long sRemove(String key, Object... values) {
        try {
            return unbox(redisTemplate.opsForSet().remove(key, values));
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 是否存在值
     *
     * @param key   键
     * @param value 值
     * @return true-存在
     */
    public boolean sSetHas(String key, Object value) {
        return unbox(redisTemplate.opsForSet().isMember(key, value));
    }

    // ============================5-zSet=============================

    /**
     * 根据key获取zSet中的所有值
     *
     * @param key 键
     * @return zSet中的所有值
     */
    public Set<Object> zSetGet(String key) {
        return sGet(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean zSetHas(String key, Object value) {
        return sSetHas(key, value);
    }

    public Boolean zSetSet(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long zSetSetWithTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return unbox(count);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return set缓存的长度
     */
    public long zSetGetSize(String key) {
        try {
            return unbox(redisTemplate.opsForSet().size(key));
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 移除值为value
     *
     * @param key    键
     * @param values 值
     * @return 移除的个数
     */
    public long zSetRemove(String key, Object... values) {
        try {
            return unbox(redisTemplate.opsForSet().remove(key, values));
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }
    // ===============================缓存值为list=================================

    public <T> List<T> lGet(String key) {
        return lGet(key, 0, -1);
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始 0 是第一个元素
     * @param end   结束 -1代表所有值
     * @return 取出来的元素 总数 end-start+1
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> lGet(String key, long start, long end) {
        try {
            return (List<T>) redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(key, e);
            return Collections.emptyList();
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return list缓存的长度
     */
    public long lSize(String key) {
        try {
            return unbox(redisTemplate.opsForList().size(key));
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 通过索引获取list中的值
     *
     * @param key   键
     * @param index 索引
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T lGetIndex(String key, long index) {
        try {
            return (T) redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 将值放入list
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public <T> boolean lSet(String key, T value) {
        try {
            return unbox(redisTemplate.opsForList().rightPush(key, value)) > 0;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将值放入list
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return true 成功 false 失败
     */
    public <T> boolean lSet(String key, T value, long time) {
        try {
            if (lSet(key, value) && time > 0) {
                return expire(key, time);
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public <T> boolean lSetList(String key, List<T> value) {
        try {
            return unbox(redisTemplate.opsForList().rightPushAll(key, value)) > 0;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return true 成功 false 失败
     */
    public boolean lSetList(String key, List<Object> value, long time) {
        try {
            if (lSetList(key, value) && time > 0) {
                return expire(key, time);
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true 成功 false 失败
     */
    public <T> boolean lUpdateByIndex(String key, long index, T value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 根据key修改vulue中的某条数据
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public <T> boolean lUpdateByValue(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value, 0);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return unbox(redisTemplate.opsForList().remove(key, count, value));
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    // ===============================分布式锁 Lock=================================
    public static final DefaultRedisScript<Long> LOCK_SCRIPT;

    public static final String LOCK_KEY_PREFIX = "redis:lock:";
    public static final String uuid = UUID.randomUUID() + ":";
    public static final String SCRIPT =
            "if (redis.call('GET', KEYS[1]) == ARGV[1]) then\n" +
                    "  -- 一致，则删除锁\n" +
                    "  return redis.call('DEL', KEYS[1])\n" +
                    "end\n" +
                    "-- 不一致，则直接返回\n" +
                    "return 0";

    static {
        LOCK_SCRIPT = new DefaultRedisScript<>();
        LOCK_SCRIPT.setScriptText(SCRIPT);
        LOCK_SCRIPT.setResultType(Long.class);
    }


    /**
     * 设置分布式锁，只适用于单机Redis，且无法重入，可以使用Redisson
     *
     * @param lockName 锁名字
     * @param duration 持续时间（要大于业务时间）
     * @param timeUnit 时间单位
     * @return boolean
     */
    public boolean tryLock(String lockName, Long duration, TimeUnit timeUnit) {
        long threadId = Thread.currentThread().getId();
        return unbox(redisTemplate.opsForValue().setIfAbsent(LOCK_KEY_PREFIX + lockName, uuid + threadId, duration, timeUnit));
    }


    /**
     * 释放分布式
     *
     * @param lockName 锁key
     * @return boolean
     */
    public boolean unlock(String lockName) {
        String threadId = uuid + Thread.currentThread().getId();
        Long isUnLock = redisTemplate.execute(LOCK_SCRIPT,
                Collections.singletonList(LOCK_KEY_PREFIX + lockName),
                threadId);
        return "1".equals(isUnLock + "");

    }

    // ===============================缓存穿透，击穿，雪崩=================================

    /**
     * 查询
     * @param key   键
     * @param duration  过期时间/秒
     * @param queryPram 查询参数
     * @param queryAction   查询行为
     * @return {@link O}
     */
    @SuppressWarnings("unchecked")
    public <I, O> O getWithPassThrough(String key, long duration, I queryPram, Function<I, O> queryAction) {

        Object object = getObject(key);
        if ("".equals(object)) {
            return null;
        }
        if (object == null) {
            // redis中没有值,去db查询
            O result = queryAction.apply(queryPram);
            // db中也没有值，缓存空字符串到Redis
            if (result == null) {
                setObject(key, "", duration);
                return null;
            }
            setObject(key, result, duration);
            return result;
        }
        expire(key, duration);

        return (O) object;
    }
}
