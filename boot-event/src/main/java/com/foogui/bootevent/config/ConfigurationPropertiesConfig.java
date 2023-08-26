package com.foogui.bootevent.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
//这里的注解必须是Configuration，不能是其他，否则PropertySource不生效
@Component
@ConfigurationProperties(prefix = "config.bean")
@PropertySource(value = "classpath:beanconfig.properties")
@Getter
@Setter
@ConditionalOnProperty(value = "config.bean.enable",havingValue = "true")
public class ConfigurationPropertiesConfig {
    private String operator;
    private Map<String, Integer> points = new HashMap<>();
}
