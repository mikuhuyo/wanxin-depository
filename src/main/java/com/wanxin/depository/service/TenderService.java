package com.wanxin.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanxin.depository.entity.Tender;
import com.wanxin.depository.model.ConfirmLoanResponse;
import com.wanxin.depository.model.UserAutoPreTransactionRequest;
import com.wanxin.depository.model.UserAutoPreTransactionResponse;

/**
 * <p>
 * 投标信息表 服务类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
public interface TenderService extends IService<Tender> {

    /**
     * 投标预授权处理
     *
     * @param preTransactionRequest
     * @return
     */
    UserAutoPreTransactionResponse autoPreTransactionForTender(UserAutoPreTransactionRequest preTransactionRequest);

    /**
     * 放款确认
     *
     * @param reqData
     * @return
     */
    ConfirmLoanResponse confirmLoan(String reqData);

}
