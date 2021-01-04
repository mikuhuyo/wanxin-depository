package com.wanxin.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanxin.depository.entity.RechargeDetails;
import com.wanxin.depository.model.RechargeRequest;
import com.wanxin.depository.model.RechargeResponse;

/**
 * <p>
 * 充值记录表 服务类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
public interface RechargeDetailsService extends IService<RechargeDetails> {

    /**
     * 用户充值
     *
     * @param rechargeRequest
     * @return
     */
    RechargeResponse recharge(RechargeRequest rechargeRequest);

}
