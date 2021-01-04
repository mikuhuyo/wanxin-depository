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
public interface BankUserService extends IService<BankUser> {

    /**
     * 根据姓名和ID获取用户信息
     *
     * @param fullname 姓名
     * @param idNumber 身份证号
     * @return
     */
    BankUser getUser(String fullname, String idNumber);

}
