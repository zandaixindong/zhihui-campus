package com.zhihui.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zhihui.common.core.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis Plus 自动填充处理器
 */
@Slf4j
@Component
public class AutoFillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        try {
            this.strictInsertFill(metaObject, "createBy", String.class, SecurityUtils.getUsername());
            this.strictInsertFill(metaObject, "updateBy", String.class, SecurityUtils.getUsername());
        } catch (Exception e) {
            log.warn("自动填充创建人信息失败: {}", e.getMessage());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        try {
            this.strictUpdateFill(metaObject, "updateBy", String.class, SecurityUtils.getUsername());
        } catch (Exception e) {
            log.warn("自动填充更新人信息失败: {}", e.getMessage());
        }
    }
}
