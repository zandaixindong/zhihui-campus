package com.zhihui.common.core.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {

    public static final String YYYY = "yyyy";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] WEEK_DAYS = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    /**
     * 获取当前日期
     */
    public static LocalDate now() {
        return LocalDate.now();
    }

    /**
     * 获取当前日期时间
     */
    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 格式化日期
     */
    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化日期时间
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期时间
     */
    public static LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Date转LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * LocalDateTime转Date
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取星期几
     */
    public static String getWeekDay(LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        return WEEK_DAYS[dayOfWeek - 1];
    }

    /**
     * 计算两个日期之间的天数
     */
    public static long betweenDays(LocalDate start, LocalDate end) {
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 获取当前学期（示例：2024-2025-1）
     * 根据月份判断：2-7月为第二学期，8-1月为第一学期
     */
    public static String getCurrentSemester() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        if (month >= 2 && month <= 7) {
            return (year - 1) + "-" + year + "-2";
        } else {
            if (month >= 8) {
                return year + "-" + (year + 1) + "-1";
            } else {
                return (year - 1) + "-" + year + "-1";
            }
        }
    }

    /**
     * 获取当前是第几周（相对于学期开始）
     * 假设第一学期9月1日开始，第二学期2月20日开始
     */
    public static int getCurrentWeek() {
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();

        LocalDate semesterStart;
        if (month >= 2 && month <= 7) {
            // 第二学期
            semesterStart = LocalDate.of(now.getYear(), 2, 20);
        } else {
            // 第一学期
            if (month >= 8) {
                semesterStart = LocalDate.of(now.getYear(), 9, 1);
            } else {
                semesterStart = LocalDate.of(now.getYear() - 1, 9, 1);
            }
        }

        long days = betweenDays(semesterStart, now);
        return (int) (days / 7) + 1;
    }
}
