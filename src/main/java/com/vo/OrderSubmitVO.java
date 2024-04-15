package com.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderSubmitVO implements Serializable {
    Long id;
    BigDecimal orderAmount;
    String orderNumber;
    LocalDateTime orderTime;
}
