package com.foogui.boot.mybatisplus.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author Foogui
 * @date 2023/07/30
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SexEnum {
    MAN(1, "男"),
    WOMAN(0, "女");
    @EnumValue
    private Integer code;
    private String name;

    SexEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
