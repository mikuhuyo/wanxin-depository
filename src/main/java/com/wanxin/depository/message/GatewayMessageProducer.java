package com.wanxin.depository.message;

import com.alibaba.fastjson.JSON;
import com.wanxin.depository.model.PersonalRegisterResponse;
import com.wanxin.depository.model.RechargeResponse;
import com.wanxin.depository.model.WithdrawResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <P>
 * 网关接口请求回调通知消息生产者
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Slf4j
@Component
public class GatewayMessageProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void personalRegister(String appCode, PersonalRegisterResponse response) {
        Message<?> message = MessageBuilder.withPayload(JSON.toJSONString(response)).setHeader(MessageConst.PROPERTY_KEYS, appCode).build();
        rocketMQTemplate.convertAndSend("TP_GATEWAY_NOTIFY:PERSONAL_REGISTER", message);
    }

    public void recharge(String appCode, RechargeResponse response) {
        Message<?> message = MessageBuilder.withPayload(JSON.toJSONString(response)).setHeader(MessageConst.PROPERTY_KEYS, appCode).build();
        rocketMQTemplate.convertAndSend("TP_GATEWAY_NOTIFY:RECHARGE", message);
    }

    public void withdraw(String appCode, WithdrawResponse response) {
        Message<?> message = MessageBuilder.withPayload(JSON.toJSONString(response)).setHeader(MessageConst.PROPERTY_KEYS, appCode).build();
        rocketMQTemplate.convertAndSend("TP_GATEWAY_NOTIFY:WITHDRAW", message);
    }

}
