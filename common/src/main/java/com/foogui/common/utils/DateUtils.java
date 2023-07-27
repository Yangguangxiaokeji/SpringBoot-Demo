package com.foogui.common.utils;

import com.foogui.common.exception.BizException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;


/**
 * 日期跑龙套
 *
 * @author Foogui
 * @date 2023/07/27
 */
public class DateUtils {
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY = "yyyy";
    public static final String MM_DD = "MM-dd";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String HH_MM = "HH:mm";

    public static final String HH = "HH";


    public static final DateTimeFormatter YEAR_MILLISECOND_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS_SSS).withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS).withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter YEAR_MINUTE_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM).withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter YEAR_HOUR_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH).withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter DATE_FORMATTER;

    public static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern(YYYY_MM).withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern(YYYY).withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter MONTH_DAY_FORMATTER = DateTimeFormatter.ofPattern(MM_DD).withZone(ZoneId.systemDefault());

    public static final DateTimeFormatter TIME_FORMATTER;

    public static final DateTimeFormatter HOUR_MINUTE_FORMATTER;

    public static final DateTimeFormatter HOUR_FORMATTER;

    private static final Map<String, DateTimeFormatter> CACHE;

    static {

        //初始化formatter
        DateTimeFormatterBuilder dateFormatterBuilder = new DateTimeFormatterBuilder();
        dateFormatterBuilder.appendPattern(YYYY_MM_DD);
        dateFormatterBuilder.parseDefaulting(ChronoField.HOUR_OF_DAY, 0);
        dateFormatterBuilder.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0);
        dateFormatterBuilder.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0);
        dateFormatterBuilder.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0);
        DATE_FORMATTER = dateFormatterBuilder.toFormatter().withZone(ZoneId.systemDefault());

        DateTimeFormatterBuilder timeFormatterBuilder = new DateTimeFormatterBuilder();
        timeFormatterBuilder.appendPattern(HH_MM_SS);
        timeFormatterBuilder.parseDefaulting(ChronoField.YEAR, 1970);
        timeFormatterBuilder.parseDefaulting(ChronoField.MONTH_OF_YEAR, 1);
        timeFormatterBuilder.parseDefaulting(ChronoField.DAY_OF_MONTH, 1);
        timeFormatterBuilder.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0);
        TIME_FORMATTER = timeFormatterBuilder.toFormatter().withZone(ZoneId.systemDefault());

        DateTimeFormatterBuilder hourMinuteFormatterBuilder = new DateTimeFormatterBuilder();
        hourMinuteFormatterBuilder.appendPattern(HH_MM);
        hourMinuteFormatterBuilder.parseDefaulting(ChronoField.YEAR, 1970);
        hourMinuteFormatterBuilder.parseDefaulting(ChronoField.MONTH_OF_YEAR, 1);
        hourMinuteFormatterBuilder.parseDefaulting(ChronoField.DAY_OF_MONTH, 1);
        hourMinuteFormatterBuilder.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0);
        hourMinuteFormatterBuilder.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0);
        HOUR_MINUTE_FORMATTER = hourMinuteFormatterBuilder.toFormatter().withZone(ZoneId.systemDefault());

        DateTimeFormatterBuilder hourFormatterBuilder = new DateTimeFormatterBuilder();
        hourFormatterBuilder.appendPattern(HH);
        hourFormatterBuilder.parseDefaulting(ChronoField.YEAR, 1970);
        hourFormatterBuilder.parseDefaulting(ChronoField.MONTH_OF_YEAR, 1);
        hourFormatterBuilder.parseDefaulting(ChronoField.DAY_OF_MONTH, 1);
        hourFormatterBuilder.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0);
        hourFormatterBuilder.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0);
        hourFormatterBuilder.parseDefaulting(ChronoField.MILLI_OF_SECOND, 0);
        HOUR_FORMATTER = hourFormatterBuilder.toFormatter().withZone(ZoneId.systemDefault());

        //初始化缓存
        Map<String, DateTimeFormatter> map = new HashMap<>();
        map.put(YYYY_MM_DD_HH_MM_SS_SSS, YEAR_MILLISECOND_FORMATTER);
        map.put(YYYY_MM_DD_HH_MM_SS, DATE_TIME_FORMATTER);
        map.put(YYYY_MM_DD_HH_MM, YEAR_MINUTE_FORMATTER);
        map.put(YYYY_MM_DD_HH, YEAR_HOUR_FORMATTER);
        map.put(YYYY_MM_DD, DATE_FORMATTER);
        map.put(YYYY_MM, YEAR_MONTH_FORMATTER);
        map.put(YYYY, YEAR_FORMATTER);
        map.put(MM_DD, MONTH_DAY_FORMATTER);
        map.put(HH_MM_SS, TIME_FORMATTER);
        map.put(HH_MM, HOUR_MINUTE_FORMATTER);
        map.put(HH, HOUR_FORMATTER);
        CACHE = Collections.unmodifiableMap(map);
    }


    /**
     * Date格式化为String
     *
     * @param date          日期
     * @param formatPattern 格式
     * @return {@link String}
     */
    public static String format(Date date, String formatPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
        return sdf.format(date);
    }


    /**
     * 将String解析为Date
     *
     * @param dateStr       日期字符串
     * @param formatPattern 格式
     * @return {@link Date}
     */
    public static Date parse(String dateStr, String formatPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);

        try {
            return sdf.parse(dateStr);
        } catch (ParseException ignored) {
            throw new BizException("unable to parse the date " + dateStr + " with pattern " + formatPattern);
        }


    }

    /**
     * 天数差距
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return {@link Long}
     */
    public static Long diffDays(Date startDate, Date endDate) {
        return ChronoUnit.DAYS.between(toLocalDate(startDate), toLocalDate(endDate));
    }

    /**
     * 把Date转换为LocalDate
     *
     * @param date 处理的日期
     * @return localDate
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }


    /**
     * 获取当前事时间
     *
     * @return {@link Date}
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取指定日期所在周的周一
     *
     * @param date 指定日期
     * @return 周一的日期
     */
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }


    /**
     * 获取指定日期下周日
     */
    public static Date getNextWeekSunday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(date));
        cal.add(Calendar.DATE, 14);
        return cal.getTime();
    }

    /**
     * 加减计算日期
     *
     * @param today         今天
     * @param calendarField 日期维度
     * @param num           需要增减的数量
     * @return date
     */
    public static Date computeDate(Date today, int calendarField, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(calendarField, num);
        return calendar.getTime();
    }

}
