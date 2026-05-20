package com.zhihui.life.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 场地预约实体
 */
@Data
@TableName("life_venue_booking")
public class VenueBooking {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long venueId;

    private Long userId;

    private LocalDate bookingDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String purpose;

    private Integer attendees;

    private String contactPhone;

    private Integer status;

    private Long approverId;

    private LocalDateTime approveTime;

    private String approveRemark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
