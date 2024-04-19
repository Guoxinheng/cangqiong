package com.service;

import com.constant.dto.EditPasswordEmployeeDTO;
import com.constant.dto.LoginDTO;

import com.constant.dto.PageEmployeeDTO;
import com.constant.dto.SaveEmployeeDTO;
import com.entity.Employee;
import com.vo.PageEmployeeVO;

public interface EmployeeService {
    Employee login(LoginDTO loginDto);

    void save(SaveEmployeeDTO saveEmployeeDTO);

    PageEmployeeVO page(PageEmployeeDTO pageEmployeeDTO);

    void updateStatus(String status, Long id);

    void updateById(Employee employee);

    Employee selectById(Long id);

    void editPassword(EditPasswordEmployeeDTO editPasswordEmployeeDTO);
}
