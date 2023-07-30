package com.foogui.common.exception;

import com.foogui.common.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 5294969255532501296L;

    /**
     * 错误编码
     */
    private int code;

    /**
     * 描述信息
     */
    private String description;

    public BizException() {
        super();
        this.code=500;
    }

    public BizException(String message) {
        super(message);
        this.code=500;
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getMessage();

    }

    public BizException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
        this.description = errorCode.getMessage();
    }
}
