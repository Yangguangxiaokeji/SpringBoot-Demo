package com.foogui.faw.dingding;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


@Configuration
@RefreshScope
@Data
public class DingConfig {

    @Value("${ding.config.app-key:dingnidf2jrqutg4ullj}")
    private String appKey;

    @Value("${ding.config.app-secret:3KfWVW9ilT9y94oDblvKPYcLcnnAqcnQO1M7EYLtVOiF5nQ47YmBB9kNgHJxmI-J}")
    private String appSecret;

}
