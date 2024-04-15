package com.vo;

import com.entity.Employee;
import lombok.Data;
import java.util.List;


@Data
public class PageEmployeeVO {
 private   Long total;
 private List<Employee> records;
}
