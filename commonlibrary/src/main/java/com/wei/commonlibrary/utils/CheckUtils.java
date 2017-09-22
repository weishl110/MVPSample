package com.wei.commonlibrary.utils;

import android.text.TextUtils;

/**
 * Created by ${wei} on 2017/8/9.
 */

public class CheckUtils {
    public static boolean checkNull(String str) {
        return TextUtils.isEmpty(str);
    }

    private static final String TAG = "debug_CheckUtils";

    /**
     * 获取字符数量
     *
     * @param str
     * @return
     */
    public static int getCharLength(String str) {
        if (checkNull(str)) {
            return 0;
        }
        int count = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String s = Integer.toBinaryString(chars[i]);
            if (s.length() > 8) {
                count += 2;
            } else {
                count++;
            }
        }
        LogUtil.e(TAG, "25.checkCharLength. = " + count);
        return count;
    }

    /**
     * 判断字符串的字符数量是否超过指定数量（count）
     *
     * @param str
     * @param count 最大字符数量
     * @return
     */
    public static boolean checkCharLength(String str, int count) {
        int charCount = getCharLength(str);
        return charCount > count;
    }
}
