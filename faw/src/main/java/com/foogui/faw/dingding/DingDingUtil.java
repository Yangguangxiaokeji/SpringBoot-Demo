package com.foogui.faw.dingding;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2DepartmentGetRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2DepartmentGetResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
@Slf4j
public class DingDingUtil {

    private final DingConfig dingConfig;

    public String getUserDeptId(String idmId, String token) {
        log.info("request.idmId:{} ", idmId);
        try {
            DingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
            OapiV2UserGetRequest req1 = new OapiV2UserGetRequest();
            req1.setUserid(idmId);
            OapiV2UserGetResponse rsp2 = client1.execute(req1, token);
            // 响应成功
            if (rsp2.getErrcode() == 0L) {
                OapiV2UserGetResponse.UserGetResponse result = rsp2.getResult();
                List<Long> deptIdList = result.getDeptIdList();
                String deptId = null;
                if (CollectionUtils.isNotEmpty(deptIdList)) {
                    deptId = String.valueOf(deptIdList.get(0));
                }
                log.info("response dept id：{}", deptId);
                return deptId;
            }
            log.error("no user idmId:{},error message:{}", idmId, rsp2.getErrmsg());
            return "";

        } catch (ApiException e) {
            log.error("getUserDeptId occurs error:{}", e.getMessage(), e);
            return "";
        }
    }


    public String getDeptNameByDeptId(String deptId, String token) {
        if (StringUtils.isBlank(deptId)) {
            log.info("invalid deptId:{}", deptId);
            return "";
        }
        try {
            DingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/get");
            OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
            req.setDeptId(Long.valueOf(deptId));
            OapiV2DepartmentGetResponse rsp = client1.execute(req, token);
            if (rsp.getErrcode() == 0L) {
                OapiV2DepartmentGetResponse.DeptGetResponse result = rsp.getResult();
                log.info("get deptId:{} dept name：{}", deptId, result.getName());
                return result.getName();
            }
            log.info("invalid deptId:{},error message:{}", deptId,rsp.getErrmsg());
            return "";

        } catch (ApiException e) {
            log.error("getDeptNameByDeptId occurs error:{}", e.getMessage(), e);
            return null;
        }
    }

    public String getToken() {
        String token = "";
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest req = new OapiGettokenRequest();
        req.setAppkey(dingConfig.getAppKey());
        req.setAppsecret(dingConfig.getAppSecret());
        req.setHttpMethod("GET");
        OapiGettokenResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        token = rsp.getAccessToken();
        log.info("get token：{}", token);
        return token;
    }


}
