package com.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SalesTop10ReportVO implements Serializable {

  private   String nameList; //商品名称列表
     private  String numberList;//销量列表

}
