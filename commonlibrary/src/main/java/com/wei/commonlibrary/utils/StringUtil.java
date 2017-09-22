package com.wei.commonlibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ${wei} on 2017/4/14.
 */

public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * 字符串转double
     *
     * @param str 必须是数值类型的
     * @return
     */
    public static double str2Double(String str) {
        if (isEmpty(str)) return 0;
        return Double.valueOf(str);
    }

    /**
     * 判断是否包含特殊字符
     *
     * @param str
     * @return
     */
    public static boolean compileExChar(String str) {
        String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(limitEx);
        Matcher m = pattern.matcher(str);
        return m.find();
    }
}
