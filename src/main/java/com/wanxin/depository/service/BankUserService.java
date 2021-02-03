package com.wanxin.depository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wanxin.depository.entity.BankUser;

/**
 * <p>
 * 银行用户信息表 服务类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
public interface BankUserService {
    /**
     * 根据手机号和ID获取用户信息
     *
     * @param mobile 手机号
     * @param idNumber 身份证号
     * @return
     */
    BankUser getUser(String mobile, String idNumber);

    /**
     * 新增账户信息
     *
     * @param bankUser 用户银行卡信息
     */
    void createUser(BankUser bankUser);
}
