package com.dto;

import lombok.Data;

@Data
public class PageEmployeeDTO {
  private   String name;
  private int page;
  private int pageSize;
}
