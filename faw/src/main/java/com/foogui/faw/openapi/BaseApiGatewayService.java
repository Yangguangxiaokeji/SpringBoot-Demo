package com.foogui.faw.openapi;


import com.fasterxml.jackson.core.type.TypeReference;
import com.foogui.common.exception.BizException;
import com.foogui.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
@Slf4j
public class BaseApiGatewayService {

    @Autowired
    private UcgConfig ucgConfig;



    private static final String UCG_GET_TOKEN_URL = "/ucg/oauth/getToken";

    /**
     * 用于从网关获取token
     * @return
     */
    public String getToken() {

        Map<String, String> params = this.buildParamsMap(ucgConfig.getAppKey(), String.valueOf(System.currentTimeMillis()));

        String signature = this.sign(params, ucgConfig.getAppSecret());

        log.info("getToken.signature: {}", signature);

        String response = HttpUtil.sendGet(getGetUrl(params.get("appKey"), params.get("timestamp"), signature),
                null, null);

        log.info("getToken.response: {}", response);

        if (StringUtils.isBlank(response)) {
            throw new BizException("getToken.response is null or blank");
        }
        Result<UcgToken> result = JsonUtil.jsonToObject(response,
                new TypeReference<Result<UcgToken>>() {
                });

        if (Objects.isNull(result) || Objects.isNull(result.getData())) {
            throw new BizException("getToken.response.result is null or blank");
        }

        return result.getData().getAccess_token();
    }

    private Map<String, String> buildParamsMap(String appKey, String timestamp) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("appKey", appKey);
        params.put("timestamp", timestamp);
        return params;
    }

    public String sign(Map<String, String> params, String secretKey) {

        log.info("sign.params: {}", JsonUtil.toJsonString(params));

        Map<String, String> treeMap;
        if (params instanceof TreeMap) {
            treeMap = params;
        } else {
            treeMap = new TreeMap<>(params);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append(entry.getValue());
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            String base64String = Base64.getEncoder().encodeToString(signData);
            return URLEncoder.encode(base64String, "UTF-8");
        } catch (Exception e) {
            log.error("sign.failed", e);
            throw new BizException("rpc failed");
        }
    }

    private String getGetUrl(String appKey, String timestamp, String signature) {
        return String.format("%s?appKey=%s&timestamp=%s&signature=%s",
                ucgConfig.getHost() + UCG_GET_TOKEN_URL, appKey, timestamp, signature);
    }

    public String buildApiRequestUrl(String url, String token) {
        return String.format("%s?access_token=%s", url, token);
    }

}
