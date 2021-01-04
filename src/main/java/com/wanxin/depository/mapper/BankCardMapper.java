package com.wanxin.depository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanxin.depository.entity.BankCard;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 银行用户银行卡信息 Mapper 接口
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Repository
public interface BankCardMapper extends BaseMapper<BankCard> {

//	Page<BankCard> queryBankCards(Page page, @Param(value = "card") BankCard bankCard);

}
