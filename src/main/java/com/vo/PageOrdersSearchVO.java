package com.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageOrdersSearchVO implements Serializable {
    Long total;
    List<OrderDetailListVO> records;
}
