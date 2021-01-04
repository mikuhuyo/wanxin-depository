package com.wanxin.depository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 存管系统请求信息表
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Data
@Accessors(chain = true)
@TableName("request_details")
public class RequestDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 应用编码
     */
    @TableField("APP_CODE")
    private String appCode;

    /**
     * 请求流水号
     */
    @TableField("REQUEST_NO")
    private String requestNo;

    /**
     * 接口名称
     */
    @TableField("SERVICE_NAME")
    private String serviceName;

    /**
     * 请求数据
     */
    @TableField("REQUEST_DATA")
    private String requestData;

    /**
     * 返回数据
     */
    @TableField("RESPONSE_DATA")
    private String responseData;

    /**
     * 执行结果
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 请求时间
     */
    @TableField(value = "CREATE_DATE", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 执行返回时间
     */
    @TableField(value = "FINISH_DATE", fill = FieldFill.UPDATE)
    private LocalDateTime finishDate;


}
