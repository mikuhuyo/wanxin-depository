package com.wanxin.depository.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <P>
 * 预授权处理返回信息
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Data
@ApiModel(value = "UserAutoPreTransactionResponse", description = "预授权处理返回信息")
public class UserAutoPreTransactionResponse extends BaseResponse {

    @ApiModelProperty("预处理业务类型")
    private String bizType;
}
