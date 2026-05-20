package com.zhihui.life.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 一卡通消费记录实体
 */
@Data
@TableName("life_card_transaction")
public class CardTransaction {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long userId;

    private String cardNo;

    private BigDecimal amount;

    private BigDecimal balance;

    private Integer transactionType;

    private String merchantName;

    private String merchantNo;

    private LocalDateTime transactionTime;

    private Integer status;

    private LocalDateTime createTime;
}
