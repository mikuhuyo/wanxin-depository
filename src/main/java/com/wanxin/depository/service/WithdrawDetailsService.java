package com.wanxin.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanxin.depository.entity.WithdrawDetails;
import com.wanxin.depository.model.WithdrawRequest;
import com.wanxin.depository.model.WithdrawResponse;

/**
 * <p>
 * 用户余额明细表 服务类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
public interface WithdrawDetailsService extends IService<WithdrawDetails> {

    /**
     * 用户提现
     *
     * @param withdrawRequest
     * @return
     */
    WithdrawResponse withDraw(WithdrawRequest withdrawRequest);

}
