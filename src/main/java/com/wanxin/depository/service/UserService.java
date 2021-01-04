package com.wanxin.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanxin.depository.entity.DepositoryBankCard;
import com.wanxin.depository.entity.User;
import com.wanxin.depository.model.PersonalRegisterRequest;
import com.wanxin.depository.model.PersonalRegisterResponse;

/**
 * <p>
 * 存管用户信息表 服务类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
public interface UserService extends IService<User> {

    /**
     * 用户绑卡注册
     *
     * @param personalRegisterRequest
     * @return
     */
    PersonalRegisterResponse createUser(PersonalRegisterRequest personalRegisterRequest);

    /**
     * 根据用户编码获取绑定银行卡信息
     *
     * @param userNo
     * @return
     */
    DepositoryBankCard getDepositoryBankCardByUserNo(String userNo);

    /**
     * 校验用户交易密码
     *
     * @param userNo
     * @param password
     * @return
     */
    Boolean verifyPassword(String userNo, String password);

}
