package com.wanxin.depository.model;

import io.swagger.annotations.ApiModel;

/**
 * <P>
 * 本地请求响应信息
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@ApiModel(value = "LocalResponse<T>", description = "本地请求响应信息")
public class LocalResponse<T> extends BaseResponse {

    private T result;

    public static <T> LocalResponse<T> success() {
        return new LocalResponse<T>();
    }

    public static <T> LocalResponse<T> success(T result) {
        LocalResponse<T> response = new LocalResponse<T>();
        response.setResult(result);
        return response;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
