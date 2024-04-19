package com.constant.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDetailTopDTO implements Serializable {
    String name;
    Long number;
}
