package com.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DishOverViewVO implements Serializable {
  private   Long discontinued;//已经停售的菜品数量
  private   Long sold;//已经起售的菜品数量
}
