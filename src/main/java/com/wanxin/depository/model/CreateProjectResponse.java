package com.wanxin.depository.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <P>
 * 创建标的返回数据
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Data
@ApiModel(value = "CreateProjectResponse", description = "创建标的返回数据")
public class CreateProjectResponse extends BaseResponse {
    public CreateProjectResponse(String requestNo) {
        super(requestNo);
    }
}
