package com.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageOrdersVO implements Serializable {
    Long total;
    List<ListOrdersVO> records;
}
