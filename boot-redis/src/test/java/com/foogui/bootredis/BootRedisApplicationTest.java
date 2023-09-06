package com.foogui.bootredis;

import com.foogui.bootredis.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
@Slf4j
public class BootRedisApplicationTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testRedis() {
        User user = new User("wangxin", "199401");
        User user1 = new User("wangxin1", "1994011");

        redisService.setObject("wangxin", user);
        User object = (User)redisService.getObject("wangxin");
        log.info("setObject is{}", object.getPassword());
        ArrayList<User> users = new ArrayList<>(Arrays.asList(user, user1));
        redisService.lSet("users", users);
        Object users1 = redisService.lGet("users").get(0);
        log.info("lSet is{}", users1.toString());
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User implements Serializable {
        private static final long serialVersionUID = 1756966930157728667L;
        private String username;
        private String password;
    }
}
