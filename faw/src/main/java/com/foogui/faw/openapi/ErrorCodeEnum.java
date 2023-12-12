package com.foogui.faw.openapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Leon
 * @date 2022-08-31 10:21
 */
@AllArgsConstructor
@Getter
public enum ErrorCodeEnum {

    /**
     * 错误码枚举
     */
    INTERNAL_FAILED(500, "服务器内部错误"),

    TOO_MANY_DATA(501, "数据过长"),

    PARAMETER_ERROR(502, "错误的参数"),

    DATA_EXCEPTION(503, "数据异常"),

    AUTH_EXCEPTION(504, "无权限"),

    DATA_EXIST(505, "数据已存在"),

    DATE_FORMAT_ERROR(506, "日期格式不正确"),

    DATE_RANGE_ERROR(507, "日期范围不正确"),

    DISTRIBUTED_LOCK_FAILED(508, "分布式锁失败"),

    REQUEST_PARAM_NULL(509, "请求参数为空"),

    TASK_INSTANCE_CODE_EXCEPTION(517, "任务实例编码缺失"),

    RPC_FAILED(600, "rpc请求失败"),

    HTTP_REQUIRE_FAILED(601, "HTTP请求失败"),

    HTTP_RESPONSE_FAILED(602, "HTTP响应失败"),

    DATA_PERSISTENCE_FAILED(700, "数据持久化失败"),

    UPLOAD_FILE_ERROR(800,"上传文件失败"),

    DELETE_FILE_ERROR(801,"删除文件失败"),

    GET_TOKEN_FAILED(900, "获取token失败"),

    ENUM_TYPE_ERROR(901, "枚举类型code错误"),


    ALLOT_TASK_FAILED(906, "分配任务失败"),

    PROCESS_CATEGORY_NOT_FOUND(6016, "流程分类码对应业务实现不存在，请确认"),

    ;

    private final int code;

    private final String desc;
}
