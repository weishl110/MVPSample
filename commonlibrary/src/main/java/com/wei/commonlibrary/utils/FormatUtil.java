package com.wei.commonlibrary.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by ${wei} on 2017/3/28.
 */

public class FormatUtil {

    /**
     * 转换日期格式
     *
     * @param date     日期字符串
     * @param oldRegex 原来的日期格式 必须和日期格式保持一致
     * @param newRegex 新的日期格式
     * @return
     */
    public static String formatDateStr(String date, String oldRegex, String newRegex) {
        if (TextUtils.isEmpty(oldRegex) || TextUtils.isEmpty(newRegex)) {
            throw new IllegalArgumentException("日期格式不能为空");
        }

        if (date.contains(".")) {
            date = date.substring(0, date.indexOf('.'));
        }

        if (!Pattern.matches(oldRegex, date))
            throw new IllegalArgumentException("日期格式不匹配");
        SimpleDateFormat format = new SimpleDateFormat(oldRegex);
        date = format.format(date);

        format = new SimpleDateFormat(newRegex);
        try {
            Date parse = format.parse(date);
            return format.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatDate(Date date, String newRegex) {
        SimpleDateFormat format = new SimpleDateFormat(newRegex);
        String strDate = format.format(date);
        return strDate;
    }

    /**
     * 将日期装换为毫秒
     *
     * @param date
     * @param regex
     * @return
     */
    public static long formatDate(String date, String regex) {
        if (TextUtils.isEmpty(date)) {
            return 0;
        }
        SimpleDateFormat format = new SimpleDateFormat(regex);
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 手机号匹配
     *
     * @param phone
     * @return
     */
    public static boolean regexPhone(String phone) {
        String regex = "^1[3|5|7|8]\\d{9}$";
        if (StringUtil.isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(regex, phone);
    }
}
