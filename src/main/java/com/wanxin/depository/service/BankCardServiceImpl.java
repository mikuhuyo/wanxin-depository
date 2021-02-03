package com.wanxin.depository.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectOne;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wanxin.depository.common.constant.BalanceChangeCode;
import com.wanxin.depository.common.domain.BusinessException;
import com.wanxin.depository.common.domain.LocalReturnCode;
import com.wanxin.depository.common.domain.PageVO;
import com.wanxin.depository.common.domain.RemoteReturnCode;
import com.wanxin.depository.entity.BankCard;
import com.wanxin.depository.entity.BankCardDetails;
import com.wanxin.depository.entity.BankUser;
import com.wanxin.depository.mapper.BankCardDetailsMapper;
import com.wanxin.depository.mapper.BankCardMapper;
import com.wanxin.depository.model.BankCardDTO;
import com.wanxin.depository.model.BankCardQuery;
import com.wanxin.depository.model.BankCardRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 银行用户银行卡信息 服务实现类
 * </p>
 *
 * @author yuelimin
 * @since 1.8
 */
@Slf4j
@Service
public class BankCardServiceImpl extends ServiceImpl<BankCardMapper, BankCard> implements BankCardService {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private BankCardDetailsMapper bankCardDetailsMapper;

    @Autowired
    private BankUserService bankUserService;


    @Override
    @Transactional
    public String createBankCard(BankCardRequest bankCardRequest) {
        BankUser bankUser = bankUserService.getUser(bankCardRequest.getFullname(), bankCardRequest.getIdNumber());
        if (bankUser == null) {
            bankUser = new BankUser();
            BeanUtils.copyProperties(bankCardRequest, bankUser);
            bankUser.setUserType(1);
            bankUserService.createUser(bankUser);
        }

        BankCard bankCard = getByCardNumber(bankCardRequest.getCardNumber());
        if (bankCard != null) {
            throw new BusinessException(LocalReturnCode.E_200101);
        }
        bankCard = new BankCard();
        BeanUtils.copyProperties(bankCardRequest, bankCard);
        bankCard.setUserId(bankUser.getId());
        save(bankCard);

        BankCardDetails bankCardDetails = new BankCardDetails();
        bankCardDetails.setBankCardId(bankCard.getId());
        bankCardDetails.setMoney(bankCardRequest.getBalance());
        bankCardDetails.setBalance(bankCardRequest.getBalance());
        bankCardDetails.setChangeType(BalanceChangeCode.INCREASE.getCode());
        bankCardDetailsMapper.insert(bankCardDetails);
        return bankCard.toString();
    }

    @Override
    public BigDecimal getBalance(String cardNumber) {
        BankCard bankCard = getByCardNumber(cardNumber);
        if (bankCard == null) {
            throw new BusinessException(LocalReturnCode.E_200102);
        }
        BankCardDetails bankCardDetails = getBalanceByBankCardId(bankCard.getId());
        if (bankCardDetails != null) {
            return bankCardDetails.getBalance();
        }

        return new BigDecimal("0");
    }

    @Override
    public PageVO<BankCardDTO> queryBankCards(BankCardQuery bankCardQuery, Integer pageNo, Integer pageSize,
                                              String sortBy, String order) {
        QueryWrapper<BankCard> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(bankCardQuery.getCardNumber())) {
            queryWrapper.lambda().like(BankCard::getCardNumber, bankCardQuery.getCardNumber());
        }

        if (StringUtils.isNotBlank(bankCardQuery.getBankCode())) {
            queryWrapper.lambda().like(BankCard::getBankCode, bankCardQuery.getBankCode());
        }

        if (StringUtils.isNotBlank(sortBy)) {
            if (StringUtils.isNotBlank(order) && order.toLowerCase().equals("desc")) {
                queryWrapper.orderByDesc(sortBy);
            } else {
                queryWrapper.orderByAsc(sortBy);
            }
        }

        Page<BankCard> page = new Page<>(pageNo, pageSize);
        IPage<BankCard> bankCardIPage = bankCardMapper.selectPage(page, queryWrapper);
        List<BankCardDTO> bankCardDTOList = convertBankCardEntityListToDTOList(bankCardIPage.getRecords());
        PageVO<BankCardDTO> pageVO = new PageVO<>(bankCardDTOList, bankCardIPage.getTotal(), pageNo, pageSize);
        return pageVO;
    }

    @Override
    public Boolean verify(BankCard bankCard) {
        return getOne(new QueryWrapper<>(bankCard).lambda().eq(BankCard::getCardNumber, bankCard.getCardNumber()).eq(BankCard::getUserId, bankCard.getUserId())) == null;
    }

    @Override
    public Boolean increaseBalance(String cardNumber, BigDecimal amount) {
        BankCard bankCard = getByCardNumber(cardNumber);
        BankCardDetails details = getBalanceByBankCardId(bankCard.getId());

        BankCardDetails newDetails = new BankCardDetails();
        newDetails.setBankCardId(details.getBankCardId());
        newDetails.setChangeType(1);
        newDetails.setMoney(amount);
        newDetails.setBalance(details.getBalance().add(amount));
        return bankCardDetailsMapper.insert(newDetails) > 0;
    }

    @Override
    public Boolean decreaseBalance(String cardNumber, BigDecimal amount) {
        BankCard bankCard = getByCardNumber(cardNumber);
        BankCardDetails details = getBalanceByBankCardId(bankCard.getId());

        //判断银行卡余额是否充足
        if (details.getBalance().compareTo(amount) < 0) {
            throw new BusinessException(RemoteReturnCode.BANK_CARD_BALANCE_NOT_ENOUGH.getDesc());
        }

        BankCardDetails newDetails = new BankCardDetails();
        newDetails.setBankCardId(details.getBankCardId());
        newDetails.setChangeType(0);
        newDetails.setMoney(amount);
        newDetails.setBalance(details.getBalance().subtract(amount));
        return bankCardDetailsMapper.insert(newDetails) > 0;
    }

    @Override
    public BankCard getByCardNumber(String cardNumber) {
        return getOne(new QueryWrapper<BankCard>().lambda().eq(BankCard::getCardNumber, cardNumber), false);
    }

    /**
     * 根据银行卡ID获取余额信息
     *
     * @param bankCardId
     * @return
     */
    private BankCardDetails getBalanceByBankCardId(Long bankCardId) {
        return bankCardDetailsMapper.selectByBankCardId(bankCardId);
    }

    /**
     * entity集合转为dto集合
     *
     * @param list
     * @return
     */
    private List<BankCardDTO> convertBankCardEntityListToDTOList(List<BankCard> list) {
        if (list == null) {
            return null;
        }
        List<BankCardDTO> dtoList = new ArrayList<>();
        list.forEach(consumer -> dtoList.add(convertBankCardEntityToDTO(consumer)));
        return dtoList;
    }

    private BankCardDTO convertBankCardEntityToDTO(BankCard entity) {
        if (entity == null) {
            return null;
        }
        BankCardDTO dto = new BankCardDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
