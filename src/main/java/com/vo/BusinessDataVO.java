package com.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BusinessDataVO implements Serializable {
    private Long newUsers;//新增用户数
    private Double orderCompletionRate;//订单完成率
    private Double turnover;//营业额
    private     Double unitPrice;//平均客单价
    private    Double validOrderCount;//有效订单数
}
