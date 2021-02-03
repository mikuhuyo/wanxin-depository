package com.wanxin.depository.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wanxin.depository.common.domain.BusinessException;
import com.wanxin.depository.common.domain.LocalReturnCode;
import com.wanxin.depository.entity.BankUser;
import com.wanxin.depository.mapper.BankUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 银行用户信息表 服务实现类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Slf4j
@Service
public class BankUserServiceImpl implements BankUserService {
    @Autowired
    private BankUserMapper bankUserMapper;

    @Override
    public BankUser getUser(String mobile, String idNumber) {
        return bankUserMapper.selectOne(new LambdaQueryWrapper<BankUser>().eq(BankUser::getMobile, mobile).eq(BankUser::getIdNumber, idNumber));
    }

    @Override
    public void createUser(BankUser bankUser) {
        if (getUser(bankUser.getMobile(), bankUser.getIdNumber()) == null) {
            bankUserMapper.insert(bankUser);
        }
    }
}
