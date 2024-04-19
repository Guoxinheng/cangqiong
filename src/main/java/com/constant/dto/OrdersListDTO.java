package com.constant.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersListDTO implements Serializable {
    int page;
    int pageSize;
    Integer status;
}
