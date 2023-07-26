package com.foogui.foo.generator.enums;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum TypeEnum {

    VARCHAR("varchar","String","VARCHAR"),
    DATETIME("datetime","Date","DATE"),
    TINYINT("tinyint","Integer","TINYINT"),
    INTEGER("int","Integer","INTEGER"),
    DECIMAL("decimal","BigDecimal","DECIMAL"),
    CHAR("char","String","VARCHAR"),
    BIGINT("bigint","Long","BIGINT");

    private final static Table<String,String,String> TABLE= HashBasedTable.create();

    static {
        Stream.of(TypeEnum.values()).forEach(typeEnum -> {
            TABLE.put(typeEnum.getColumnType(),typeEnum.getJavaType(), typeEnum.getJdbcType());
        });
    }

    private String columnType;

    private String javaType;

    private String jdbcType;

    public static String getJavaType(String columnType) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.columnType.equals(columnType)) {
                return typeEnum.javaType;
            }
        }
        return null;
    }

    public static String getJdbcType(String columnType) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.columnType.equals(columnType)) {
                return typeEnum.jdbcType;
            }
        }
        return null;
    }


}
