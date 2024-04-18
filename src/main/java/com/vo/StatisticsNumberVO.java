package com.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StatisticsNumberVO implements Serializable {
    Integer confirmed;//待派送数量
    Integer deliveryInProgress;//派送中数量
    Integer toBeConfirmed;//待接单数量
}
