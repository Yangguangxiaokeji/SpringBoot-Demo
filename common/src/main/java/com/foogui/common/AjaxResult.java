package com.foogui.common;

public class AjaxResult {

    private int code;

    private boolean success;

    private String message;

    private Object object;

    public AjaxResult(int code, boolean success, String message, Object object) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.object = object;
    }

    public static AjaxResult success(int code, String message, Object object){
        return new AjaxResult(code, true, message, object);
    }
    public static AjaxResult success(Object object){
        return new AjaxResult(200, true, "成功", object);
    }

    public static AjaxResult fail(int code, String message, Object object){
        return new AjaxResult(code, false, message, object);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
