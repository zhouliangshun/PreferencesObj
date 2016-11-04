package com.kylins.obj.pref;

/**
 * Created by j-zhouliangshun on 2016/6/24.
 */
public class StringUtils {


    /**
     * 把字符串首字母大写
     * @param name
     * @return
     */
    public static String upperFristChar(String name) {
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }

    /**
     * 把首字母变成小写
     * @param name
     * @return
     */
    public static String lowerFristChar(String name) {
        char[] cs=name.toCharArray();
        cs[0]+=32;
        return String.valueOf(cs);

    }
}
