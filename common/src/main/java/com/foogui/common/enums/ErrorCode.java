package com.foogui.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * http响应码
 *
 * @author Foogui
 * @date 2023/05/02
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(200, "操作成功"),

    REDIRECT(302, "重定向"),

    UNAUTHORIZED(401, "未授权"),

    FORBIDDEN_ACCESS(403, "权限不足禁止访问"),

    NOT_FOUND(404, "资源未找到"),

    WRONG_REQUEST_METHOD(405, "请求方式错误"),

    PARAM_ERROR(400, "请求参数错误"),

    ERROR(500, "服务器内部出现错误"),

    FAIL(501, "操作失败"),

    VALIDATION_ERROR(5002, "错误的参数"),

    UN_KNOWN(50001, "未知错误");

    /**
     * 编码
     */
    private final int code;

    /**
     * 描述信息
     */
    private final String message;

    /**
     * 映射关系缓存
     */
    private final static Map<Integer, String> CODE_MAP;

    static {
        CODE_MAP = new HashMap<>();
        Stream.of(ErrorCode.values()).forEach(o -> {
            CODE_MAP.put(o.getCode(), o.getMessage());
        });
    }

    public static String getMessageByCode(int code) {
        return CODE_MAP.get(code);
    }


}
