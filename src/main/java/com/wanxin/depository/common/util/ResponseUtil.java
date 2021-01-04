package com.wanxin.depository.common.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <P>
 * P2P平台请求返回数据工具类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
public class ResponseUtil {

    /**
     * 设置HttpResponse返回指定数据
     *
     * @param response
     * @param s
     */
    public static void responseOut(HttpServletResponse response, String s) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
