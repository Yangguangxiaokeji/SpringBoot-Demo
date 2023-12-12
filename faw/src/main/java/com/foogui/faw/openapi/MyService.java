package com.foogui.faw.openapi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.foogui.common.enums.ErrorCode;
import com.foogui.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class MyService {

    private static final String TARGET_URL = "/JT/SA/SA-0214/RWG/instance/transmissionLighthouse?access_token=";
    @Autowired
    private UcgConfig ucgConfig;

    @Autowired
    private BaseApiGatewayService baseApiGatewayService;



    public void requestOpenApi(MyRequest request) {

        String token=baseApiGatewayService.getToken();
        // 调用接口
        log.info("requestOpenApi.request.value: {}", request);

        String response = HttpUtil.sendPostFlow(ucgConfig.getHost() + TARGET_URL + token, BeanUtil.beanToMap(request), ContentType.APPLICATION_JSON);

        log.info("requestOpenApi.response.value: {}", response);

        if (StringUtils.isBlank(response)) {
            log.error("requestOpenApi.response is null or blank");
        }

        // 处理数据
        Result<JSONObject> result = JsonUtil.jsonToObject(response, new TypeReference<Result<JSONObject>>() {
        });

        if (Objects.isNull(result)) {
            log.error("requestOpenApi.result is null or blank");
        }

        if (!Objects.equals(result.getCode(), ErrorCode.SUCCESS.getCode())) {
            log.error("requestOpenApi.failed.message: {}", result.getMessage());
        }else {
            log.info("requestOpenApi success");
        }

    }
}
