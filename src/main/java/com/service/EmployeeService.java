package com.service;

import com.dto.EditPasswordEmployeeDTO;
import com.dto.LoginDTO;

import com.dto.PageEmployeeDTO;
import com.dto.SaveEmployeeDTO;
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
