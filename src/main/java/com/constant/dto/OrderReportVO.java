package com.constant.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderReportVO implements Serializable {
        String dateList;//日期列表
        Double  orderCompletionRate;//订单完成率
        String  orderCountList;//订单数列表
        Double    totalOrderCount;//订单总数
        Double    validOrderCount;//有效订单数
        String  validOrderCountList;//有效订单数列表


}
