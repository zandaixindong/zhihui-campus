package com.zhihui.life.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.life.entity.CardTransaction;

import java.math.BigDecimal;

/**
 * 一卡通服务接口
 */
public interface CardService extends IService<CardTransaction> {

    /**
     * 查询用户余额
     */
    BigDecimal getBalance(Long userId);

    /**
     * 分页查询消费记录
     */
    PageResult<CardTransaction> getTransactionList(Long userId, Integer transactionType,
                                                    int pageNum, int pageSize);

    /**
     * 充值
     */
    void recharge(Long userId, BigDecimal amount);

    /**
     * 消费
     */
    void consume(Long userId, BigDecimal amount, String merchantName);
}
