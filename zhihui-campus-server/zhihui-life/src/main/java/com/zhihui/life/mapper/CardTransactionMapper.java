package com.zhihui.life.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.life.entity.CardTransaction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 一卡通消费记录Mapper接口
 */
@Mapper
public interface CardTransactionMapper extends BaseMapper<CardTransaction> {

    /**
     * 查询用户余额
     */
    BigDecimal selectBalance(@Param("userId") Long userId);
}
