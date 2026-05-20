package com.zhihui.life.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.life.entity.CardTransaction;
import com.zhihui.life.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 一卡通控制器
 */
@Tag(name = "一卡通管理", description = "一卡通余额查询、消费记录等接口")
@RestController
@RequestMapping("/life/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    /**
     * 查询余额
     */
    @Operation(summary = "查询余额")
    @GetMapping("/balance")
    public R<BigDecimal> getBalance(@RequestParam Long userId) {
        return R.ok(cardService.getBalance(userId));
    }

    /**
     * 查询消费记录
     */
    @Operation(summary = "查询消费记录")
    @GetMapping("/transactions")
    public R<PageResult<CardTransaction>> getTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer transactionType,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(cardService.getTransactionList(userId, transactionType, pageNum, pageSize));
    }

    /**
     * 充值
     */
    @Operation(summary = "充值")
    @PostMapping("/recharge")
    public R<Void> recharge(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        cardService.recharge(userId, amount);
        return R.ok();
    }
}
