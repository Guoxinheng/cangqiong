package com.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageSetmealVO {
private   Long total;
  private List<AdminSetmeaListVO> records;
}
