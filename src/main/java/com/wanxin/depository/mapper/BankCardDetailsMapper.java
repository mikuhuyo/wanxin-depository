package com.wanxin.depository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanxin.depository.entity.BankCardDetails;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 银行卡明细 Mapper 接口
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Repository
public interface BankCardDetailsMapper extends BaseMapper<BankCardDetails> {
    /**
     * 根据银行卡ID获取余额
     *
     * @param bankCardId
     * @return
     */
    BankCardDetails selectByBankCardId(Long bankCardId);
}
