package com.wanxin.depository.controller;

import com.alibaba.fastjson.JSON;
import com.wanxin.depository.common.utils.CheckBankCardUtil;
import com.wanxin.depository.common.utils.CommonUtil;
import com.wanxin.depository.common.utils.EncryptUtil;
import com.wanxin.depository.entity.BankUser;
import com.wanxin.depository.entity.DepositoryBankCard;
import com.wanxin.depository.model.BankCardRequest;
import com.wanxin.depository.model.PersonalRegisterRequest;
import com.wanxin.depository.model.RechargeRequest;
import com.wanxin.depository.model.WithdrawRequest;
import com.wanxin.depository.service.BankCardService;
import com.wanxin.depository.service.BankUserService;
import com.wanxin.depository.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * <p>
 * 网关接口
 * </p>
 *
 * @author yuelimin
 */
@Slf4j
@Controller
@Api(value = "网关接口", tags = "Gateway", description = "网关接口API")
public class GatewayController {
    @Autowired
    private CheckBankCardUtil checkBankCardUtil;
    @Autowired
    private BankCardService bankCardService;
    @Autowired
    private UserService userService;
    @Autowired
    private BankUserService bankUserService;

    @ApiOperation("开户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "接口名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "platformNo", value = "接入平台编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "针对请求数据reqData的签名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reqData", value = "业务数据报文, JSON 格式", required = true, dataType = "String", paramType = "query"),})
    @RequestMapping(value = "/gateway", method = RequestMethod.GET, params = "serviceName=PERSONAL_REGISTER")
    public ModelAndView create(@RequestParam("serviceName") String serviceName, @RequestParam("platformNo") String platformNo, @RequestParam("signature") String signature, @RequestParam("reqData") String reqData) throws IOException {
        String decodeReqData = EncryptUtil.decodeUTF8StringBase64(reqData);
        PersonalRegisterRequest registerRequest = JSON.parseObject(decodeReqData, PersonalRegisterRequest.class);
        registerRequest.setAppCode(platformNo);
        registerRequest.setRole("B");
        registerRequest.setUserType(1);
        String[] info = checkBankCardUtil.checkBankCard(registerRequest.getCardNumber()).split("-");
        registerRequest.setBankCode(info[0]);
        registerRequest.setBankName(info[1]);

        BankCardRequest bankCardRequest = new BankCardRequest();
        BeanUtils.copyProperties(registerRequest, bankCardRequest);
        bankCardService.createBankCard(bankCardRequest);

        log.debug("开户数据: {}", JSON.toJSONString(registerRequest));

        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("consumer", registerRequest);
        return modelAndView;
    }

    @ApiOperation("充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "接口名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "platformNo", value = "接入平台编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "针对请求数据reqData的签名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reqData", value = "业务数据报文, JSON 格式", required = true, dataType = "String", paramType = "query"),})
    @RequestMapping(value = "/gateway", method = RequestMethod.GET, params = "serviceName=RECHARGE")
    public ModelAndView recharge(@RequestParam("serviceName") String serviceName, @RequestParam("platformNo") String platformNo, @RequestParam("signature") String signature, @RequestParam("reqData") String reqData) {
        String decodeReqData = EncryptUtil.decodeUTF8StringBase64(reqData);
        RechargeRequest rechargeRequest = JSON.parseObject(decodeReqData, RechargeRequest.class);
        rechargeRequest.setAppCode(platformNo);

        // 获取存管系统绑定银行卡信息
        DepositoryBankCard bankCard = userService.getDepositoryBankCardByUserNo(rechargeRequest.getUserNo());
        rechargeRequest.setBankCode(bankCard.getBankCode());
        rechargeRequest.setBankName(bankCard.getBankName());
        rechargeRequest.setCardNumber(bankCard.getCardNumber());
        rechargeRequest.setMobile(bankCard.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        rechargeRequest.setMobile(CommonUtil.hiddenMobile(bankCard.getMobile()));
        log.debug("充值数据: {}", JSON.toJSONString(rechargeRequest));

        ModelAndView modelAndView = new ModelAndView("recharge");
        modelAndView.addObject("recharge", rechargeRequest);
        return modelAndView;
    }

    @ApiOperation("提现")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "接口名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "platformNo", value = "接入平台编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "针对请求数据reqData的签名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "reqData", value = "业务数据报文, JSON 格式", required = true, dataType = "String", paramType = "query"),})
    @RequestMapping(value = "/gateway", method = RequestMethod.GET, params = "serviceName=WITHDRAW")
    public ModelAndView withdraw(@RequestParam("serviceName") String serviceName, @RequestParam("platformNo") String platformNo, @RequestParam("signature") String signature, @RequestParam("reqData") String reqData) {
        String decodeReqData = EncryptUtil.decodeUTF8StringBase64(reqData);
        WithdrawRequest withdrawRequest = JSON.parseObject(decodeReqData, WithdrawRequest.class);
        withdrawRequest.setAppCode(platformNo);

        //获取存管系统绑定银行卡信息
        DepositoryBankCard bankCard = userService.getDepositoryBankCardByUserNo(withdrawRequest.getUserNo());
        withdrawRequest.setBankCode(bankCard.getBankCode());
        withdrawRequest.setBankName(bankCard.getBankName());
        withdrawRequest.setCardNumber(bankCard.getCardNumber());
        withdrawRequest.setMobile(bankCard.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));

        log.debug("提现数据: {}", JSON.toJSONString(withdrawRequest));

        ModelAndView modelAndView = new ModelAndView("withdraw");
        modelAndView.addObject("withdraw", withdrawRequest);
        return modelAndView;
    }

}
