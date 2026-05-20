package com.zhihui.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.life.entity.CardTransaction;
import com.zhihui.life.mapper.CardTransactionMapper;
import com.zhihui.life.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 一卡通服务实现类
 */
@Service
@RequiredArgsConstructor
public class CardServiceImpl extends ServiceImpl<CardTransactionMapper, CardTransaction> implements CardService {

    private final CardTransactionMapper cardTransactionMapper;

    @Override
    public BigDecimal getBalance(Long userId) {
        BigDecimal balance = cardTransactionMapper.selectBalance(userId);
        return balance != null ? balance : BigDecimal.ZERO;
    }

    @Override
    public PageResult<CardTransaction> getTransactionList(Long userId, Integer transactionType,
                                                           int pageNum, int pageSize) {
        Page<CardTransaction> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CardTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CardTransaction::getUserId, userId);
        if (transactionType != null) {
            wrapper.eq(CardTransaction::getTransactionType, transactionType);
        }
        wrapper.orderByDesc(CardTransaction::getTransactionTime);

        Page<CardTransaction> result = cardTransactionMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void recharge(Long userId, BigDecimal amount) {
        BigDecimal currentBalance = getBalance(userId);
        BigDecimal newBalance = currentBalance.add(amount);

        CardTransaction transaction = new CardTransaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setBalance(newBalance);
        transaction.setTransactionType(2); // 充值
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setStatus(1);

        cardTransactionMapper.insert(transaction);
    }

    @Override
    @Transactional
    public void consume(Long userId, BigDecimal amount, String merchantName) {
        BigDecimal currentBalance = getBalance(userId);
        if (currentBalance.compareTo(amount) < 0) {
            throw new RuntimeException("余额不足");
        }

        BigDecimal newBalance = currentBalance.subtract(amount);

        CardTransaction transaction = new CardTransaction();
        transaction.setUserId(userId);
        transaction.setAmount(amount.negate());
        transaction.setBalance(newBalance);
        transaction.setTransactionType(1); // 消费
        transaction.setMerchantName(merchantName);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setStatus(1);

        cardTransactionMapper.insert(transaction);
    }
}
