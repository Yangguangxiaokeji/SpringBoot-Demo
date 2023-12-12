package com.foogui.faw.openapi;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;


@Configuration
@RefreshScope
@Data
public class UcgConfig {

    @Value("${ucg.config.app-key:96109904e3ed4b3d9f30bf4b3abf0c0f}")
    private String appKey;

    @Value("${ucg.config.app-secret:89fcbc8e03214d888af3d9fdcf1585af}")
    private String appSecret;

    @Value("${ucg.config.host:https://uat-api.faw.cn:30443}")
    private String host;

    private Map<String, String> systemIdMap;

    private String defaultSystemId;


    public String getSystemIdByUserGroup(String userGroup) {

        if (StringUtils.isBlank(userGroup)) {
            return defaultSystemId;
        }

        if (Objects.nonNull(systemIdMap)) {
            return systemIdMap.getOrDefault(userGroup, defaultSystemId);
        }

        return defaultSystemId;
    }


}
