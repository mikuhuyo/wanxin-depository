package com.wanxin.depository.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanxin.depository.common.constant.StatusCode;
import com.wanxin.depository.common.domain.BusinessException;
import com.wanxin.depository.common.domain.RemoteReturnCode;
import com.wanxin.depository.entity.DepositoryBankCard;
import com.wanxin.depository.entity.WithdrawDetails;
import com.wanxin.depository.mapper.WithdrawDetailsMapper;
import com.wanxin.depository.message.GatewayMessageProducer;
import com.wanxin.depository.model.WithdrawRequest;
import com.wanxin.depository.model.WithdrawResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户余额明细表 服务实现类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Slf4j
@Service
public class WithdrawDetailsServiceImpl extends ServiceImpl<WithdrawDetailsMapper, WithdrawDetails> implements WithdrawDetailsService {

    @Autowired
    private BalanceDetailsService balanceDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private GatewayMessageProducer producer;

    /**
     * 用户提现
     *
     * @param withdrawRequest
     * @return
     */
    @Override
    @Transactional
    public WithdrawResponse withDraw(WithdrawRequest withdrawRequest) {

        WithdrawResponse response = new WithdrawResponse();
        response.setRequestNo(withdrawRequest.getRequestNo());

        //保存提现记录
        WithdrawDetails withdrawDetails = new WithdrawDetails();
        BeanUtils.copyProperties(withdrawRequest, withdrawDetails);
        withdrawDetails.setStatus(StatusCode.STATUS_OUT.getCode());
        save(withdrawDetails);

        try {
            // 增加用户实体银行卡余额
            DepositoryBankCard depositoryBankCard = userService.getDepositoryBankCardByUserNo(withdrawRequest.getUserNo());
            bankCardService.increaseBalance(depositoryBankCard.getCardNumber(), withdrawRequest.getAmount());

            // 减少用户在P2P平台可用余额信息
            balanceDetailsService.withDraw(withdrawRequest);

            // 更新提现记录结果
            withdrawDetails.setStatus(StatusCode.STATUS_IN.getCode());
            updateById(withdrawDetails);

            // 产生提现成功消息
            response.setSuccess();
            producer.withdraw(withdrawRequest.getAppCode(), response);
        } catch (Exception e) {
            log.error(e.getMessage());

            // 更新提现记录结果
            withdrawDetails.setStatus(StatusCode.STATUS_FAIL.getCode());
            updateById(withdrawDetails);

            // 产生提现失败消息
            response.setFailure();
            producer.withdraw(withdrawRequest.getAppCode(), response);
            throw new BusinessException(response.getRequestNo(), RemoteReturnCode.EXCEPTION);
        }
        return response;

    }
}
