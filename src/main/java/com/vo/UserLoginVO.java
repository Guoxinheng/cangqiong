package com.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginVO implements Serializable {
    Long id;
    String openid;
    String  token;
}
