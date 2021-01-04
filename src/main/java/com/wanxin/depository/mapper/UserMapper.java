package com.wanxin.depository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wanxin.depository.entity.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 存管用户信息表 Mapper 接口
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
