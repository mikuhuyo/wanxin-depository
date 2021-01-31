package com.wanxin.depository.common.utils;

/**
 * <P>
 * 通用工具类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
public class CommonUtil {

    public static String hiddenMobile(String mobile) {
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

}
