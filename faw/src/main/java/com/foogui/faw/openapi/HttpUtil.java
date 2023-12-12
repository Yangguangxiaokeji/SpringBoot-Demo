package com.foogui.faw.openapi;

import com.foogui.common.exception.BizException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * http工具类
 *
 * @author Leon
 * @date 2022-08-24 10:11
 */
@Slf4j
@Component
public class HttpUtil {

    @Setter
    private static RequestConfig requestConfig;

    private static HttpClientBuilder httpClientBuilder;

    @Autowired
    public void setHttpClientBuilder(HttpClientBuilder httpClientBuilder) {
        HttpUtil.httpClientBuilder = httpClientBuilder;
    }

    /**
     * POST请求
     *
     * @param url    请求地址
     * @param params 参数集合
     * @return 响应json字符串
     */
    public static String sendPost(String url, Map<String, String> params, ContentType contentType) {

        CloseableHttpClient client = HttpClients.createDefault();

        HttpUriRequest post;

        if (ContentType.APPLICATION_JSON == contentType) {

            post = RequestBuilder.post(url)
                    .setEntity(new StringEntity(JsonUtil.toJsonString(params), contentType))
                    .setConfig(requestConfig)
                    .build();
        } else if (ContentType.APPLICATION_FORM_URLENCODED == contentType) {

            RequestBuilder requestBuilder = RequestBuilder.post(url).setConfig(requestConfig);

            requestBuilder.setEntity(new StringEntity(getFormParam(params), "UTF-8"));

            requestBuilder.addHeader("Content-Type", contentType.getMimeType());

            requestBuilder.addHeader("Accept", "*/*");

            post = requestBuilder.build();
        } else {

            throw new BizException(ErrorCodeEnum.PARAMETER_ERROR.getCode(), "不支持的content类型");

        }

        try {

            HttpResponse response = client.execute(post);

            return EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String sendPostNew(String url, String bodyJson, Map<String, String> headers) {
        CloseableHttpClient client = httpClientBuilder.build();
        HttpUriRequest post = RequestBuilder.post(url).setEntity(new StringEntity(bodyJson, ContentType.APPLICATION_JSON)).setConfig(requestConfig).build();
        if (Objects.nonNull(headers) && !headers.isEmpty()) {
            headers.forEach(post::addHeader);
        }

        HttpResponse response = null;

        try {
            log.info("sendPost.url:{}", post.getRequestLine());
            response = client.execute(post);
            String var6 = EntityUtils.toString(response.getEntity());
            return var6;
        } catch (IOException var10) {
            var10.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(response);
        }

        return "";
    }

    private static String getFormParam(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.substring(0, sb.length() - 1);
    }

    private static String getFormParamFlow(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * GET请求
     *
     * @param url      请求地址
     * @param paramMap 参数列表
     * @return 响应json字符串
     */
    public static String sendGet(String url, Map<String, String> paramMap, Map<String, String> headers) {

        CloseableHttpClient client = HttpClients.createDefault();

        RequestBuilder requestBuilder = RequestBuilder.get(url);

        if (MapUtils.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::addHeader);
        }

        if (MapUtils.isNotEmpty(paramMap)) {
            paramMap.forEach(requestBuilder::addParameter);
        }

        HttpUriRequest get = requestBuilder
                .setConfig(requestConfig)
                .build();

        log.info("sendGet.url: {}", get.getRequestLine());

        CloseableHttpResponse response = null;
        try {

            response = client.execute(get);

            return EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(response);
        }

        return "";
    }

    public static String sendPostFlow(String url, Map<String, Object> params, ContentType contentType) {

        CloseableHttpClient client = HttpClients.createDefault();

        HttpUriRequest post;

        if (ContentType.APPLICATION_JSON == contentType) {

            post = RequestBuilder.post(url)
                    .setEntity(new StringEntity(JsonUtil.toJsonString(params), contentType))
                    .setConfig(requestConfig)
                    .build();
        } else if (ContentType.APPLICATION_FORM_URLENCODED == contentType) {

            RequestBuilder requestBuilder = RequestBuilder.post(url).setConfig(requestConfig);

            requestBuilder.setEntity(new StringEntity(getFormParamFlow(params), "UTF-8"));

            requestBuilder.addHeader("Content-Type", contentType.getMimeType());

            requestBuilder.addHeader("Accept", "*/*");

            post = requestBuilder.build();
        } else {

            throw new BizException(ErrorCodeEnum.PARAMETER_ERROR.getCode(), "不支持的content类型");

        }

        try {

            HttpResponse response = client.execute(post);

            return EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}

