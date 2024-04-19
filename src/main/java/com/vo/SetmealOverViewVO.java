package com.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SetmealOverViewVO implements Serializable {
    private   Long discontinued;
    private   Long sold;
}
