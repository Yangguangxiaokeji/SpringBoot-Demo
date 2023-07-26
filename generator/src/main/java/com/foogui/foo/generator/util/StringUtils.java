package com.foogui.foo.generator.util;

import java.util.UUID;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * 驼峰形式转下划线
     *
     * @param input 输入
     * @return {@link String}
     */
    public static String camelToUnderline(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转骆驼
     *
     * @param source 源
     * @return {@link String}
     */
    public static String underlineToCamel(String source) {
        if (source == null || source.isEmpty()) {
            return source;
        }
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (c == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 删去掉前缀并转驼峰
     *
     * @param input  输入
     * @param prefix 前缀
     * @return {@link String}
     */
    public static String removePrefixAndToCamel(String input, String prefix) {
        String first = input.replaceFirst("^" + prefix, "");
        return underlineToCamel(first);
    }


    public static String capitalizeFirstChar(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * 删除前缀和下划线
     *
     * @param input  输入
     * @param prefix 前缀
     * @return {@link String}
     */
    public static String removePrefixAndUnderline(String input, String prefix) {
        String first = input.replaceFirst("^" + prefix, "");
        return StringUtils.replace(first, "_", "");
    }

    /**
     * 去除飘号
     *
     * @param input 输入
     * @return {@link String}
     */
    public static String removeFly(String input) {
        return input.replaceAll("`", "");
    }



    /**
     * 获得去掉 "-" 的uuid
     *
     * @return {@link String}
     */
    public static String getSimpleUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
