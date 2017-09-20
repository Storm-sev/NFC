package com.cheng315.nfc.utils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/30.
 *
 */

public class StringUtils {



    /**
     * 判断传入的字符串是否为一个链接
     */
    public static boolean checkIsNet(String content) {

//        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$");

        return pattern.matcher(content).matches();
    }


    /**
     * 判断是否包含某个字符串
     */
    public static boolean isContains(String content) {

        String subStr = "315cheng";

        return content.contains(subStr);
    }

}
