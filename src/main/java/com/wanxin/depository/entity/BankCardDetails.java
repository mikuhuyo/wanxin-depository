package com.wanxin.depository.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 银行卡明细
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Data
@TableName("bank_card_details")
public class BankCardDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 银行卡ID
     */
    @TableField("BANK_CARD_ID")
    private Long bankCardId;

    /**
     * 账户变动类型
     */
    @TableField("CHANGE_TYPE")
    private Integer changeType;

    /**
     * 变动金额
     */
    @TableField("MONEY")
    private BigDecimal money;

    /**
     * 当前余额
     */
    @TableField("BALANCE")
    private BigDecimal balance;

    /**
     * 账户变动时间
     */
    @TableField(value = "CREATE_DATE", fill = FieldFill.INSERT)
    private LocalDateTime createDate;

}
