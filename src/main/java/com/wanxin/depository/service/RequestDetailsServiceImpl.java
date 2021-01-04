package com.wanxin.depository.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanxin.depository.common.constant.StatusCode;
import com.wanxin.depository.entity.RequestDetails;
import com.wanxin.depository.mapper.RequestDetailsMapper;
import com.wanxin.depository.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 存管系统请求信息表 服务实现类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Slf4j
@Service
public class RequestDetailsServiceImpl extends ServiceImpl<RequestDetailsMapper, RequestDetails>
        implements RequestDetailsService {

    @Override
    public RequestDetails create(RequestDetails requestDetails) {
        requestDetails.setStatus(StatusCode.STATUS_OUT.getCode());
        save(requestDetails);
        return requestDetails;
    }

    @Override
    public Boolean modifyByRequestNo(RequestDetails requestDetails) {
        return update(requestDetails, new QueryWrapper<RequestDetails>().lambda()
                .eq(RequestDetails::getRequestNo, requestDetails.getRequestNo()));
    }

    @Override
    public Boolean modifyGatewayByRequestNo(BaseResponse response) {
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setRequestNo(response.getRequestNo());
        requestDetails.setStatus(response.getStatus());
        requestDetails.setResponseData(JSON.toJSONString(response));
        return modifyByRequestNo(requestDetails);
    }

    @Override
    public RequestDetails getByRequestNo(String requestNo) {
        return getOne(new QueryWrapper<RequestDetails>().lambda().eq(RequestDetails::getRequestNo, requestNo), false);
    }
}
