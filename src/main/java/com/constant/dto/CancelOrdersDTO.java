package com.constant.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CancelOrdersDTO implements Serializable {
    Long id;
    String cancelReason;
}
