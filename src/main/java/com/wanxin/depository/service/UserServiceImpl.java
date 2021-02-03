package com.wanxin.depository.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanxin.depository.common.domain.BusinessException;
import com.wanxin.depository.common.domain.LocalReturnCode;
import com.wanxin.depository.common.domain.RemoteReturnCode;
import com.wanxin.depository.entity.*;
import com.wanxin.depository.mapper.DepositoryBankCardMapper;
import com.wanxin.depository.mapper.UserMapper;
import com.wanxin.depository.message.GatewayMessageProducer;
import com.wanxin.depository.model.PersonalRegisterRequest;
import com.wanxin.depository.model.PersonalRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 存管用户信息表 服务实现类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private BankUserService bankUserService;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private DepositoryBankCardMapper depositoryBankCardMapper;

    @Autowired
    private BalanceDetailsService balanceDetailsService;

    @Autowired
    private GatewayMessageProducer producer;

    @Autowired
    private RequestDetailsService requestDetailsService;

    @Override
    @Transactional
    public PersonalRegisterResponse createUser(PersonalRegisterRequest personalRegisterRequest) {
        String requestNo = personalRegisterRequest.getRequestNo();
        PersonalRegisterResponse response = new PersonalRegisterResponse();
        response.setRequestNo(requestNo);
        response.setBankCode(personalRegisterRequest.getBankCode());
        response.setBankName(personalRegisterRequest.getBankName());

        // 校验个人信息
        BankUser bankUser = bankUserService.getUser(personalRegisterRequest.getMobile(), personalRegisterRequest.getIdNumber());
        if (bankUser == null) {
            throw new BusinessException(LocalReturnCode.E_200301.getDesc());
        }

        // 校验银行实体卡
        BankCard bankCard = new BankCard();
        bankCard.setUserId(bankUser.getId());
        bankCard.setBankCode(personalRegisterRequest.getBankCode());
        bankCard.setBankName(personalRegisterRequest.getBankName());
        bankCard.setCardNumber(personalRegisterRequest.getCardNumber());
        bankCard.setPassword(personalRegisterRequest.getPassword());
        if (!bankCardService.verify(bankCard)) {
            throw new BusinessException(LocalReturnCode.E_200103);
        }
        bankCardService.save(bankCard);

        try {
            // 保存开户信息
            User user = new User();
            BeanUtils.copyProperties(personalRegisterRequest, user);
            user.setIsBindCard(1);
            user.setAuthList("ALL");
            // 用户类型默认为个人
            user.setUserType(1);
            save(user);

            // 保存绑定银行卡信息
            bankCard = bankCardService.getByCardNumber(personalRegisterRequest.getCardNumber());
            DepositoryBankCard depositoryBankCard = new DepositoryBankCard();
            depositoryBankCard.setAppCode(personalRegisterRequest.getAppCode());
            BeanUtils.copyProperties(bankCard, depositoryBankCard);
            depositoryBankCard.setMobile(user.getMobile());
            depositoryBankCard.setUserId(user.getId());
            depositoryBankCard.setRequestNo(user.getRequestNo());
            depositoryBankCardMapper.insert(depositoryBankCard);

            // 设置初始余额信息
            balanceDetailsService.addForPersonalRegister(new BalanceDetails()
                    .setUserNo(user.getUserNo())
                    .setAppCode(personalRegisterRequest.getAppCode())
                    .setRequestContent(JSON.toJSONString(personalRegisterRequest)));

            // 更新处理结果
            response.setSuccess();
            requestDetailsService.modifyGatewayByRequestNo(response);

            // 产生开户成功消息
            producer.personalRegister(personalRegisterRequest.getAppCode(), response);
        } catch (Exception e) {
            log.error(e.getMessage());

            // 更新处理结果
            response.setFailure();
            requestDetailsService.modifyGatewayByRequestNo(response);

            // 产生开户失败消息
            producer.personalRegister(personalRegisterRequest.getAppCode(), response);
            throw new BusinessException(personalRegisterRequest.getRequestNo(), RemoteReturnCode.EXCEPTION);
        }

        return response;
    }

    @Override
    public DepositoryBankCard getDepositoryBankCardByUserNo(String userNo) {
        User user = getByUserNo(userNo);
        return depositoryBankCardMapper.selectOne(Wrappers.<DepositoryBankCard>query().lambda().eq(DepositoryBankCard::getUserId, user.getId()));
    }

    @Override
    public Boolean verifyPassword(String userNo, String password) {
        User user = getByUserNo(userNo);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }

    /**
     * 根据用户编号获取用户信息
     *
     * @param userNo
     * @return
     */
    private User getByUserNo(String userNo) {
        return getOne(Wrappers.<User>query().lambda().eq(User::getUserNo, userNo), false);
    }
}
