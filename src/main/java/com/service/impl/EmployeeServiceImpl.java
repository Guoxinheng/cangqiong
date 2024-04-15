package com.service.impl;

import com.constant.MessageConstant;
import com.context.BaseContext;
import com.dto.EditPasswordEmployeeDTO;
import com.dto.LoginDTO;
import com.dto.PageEmployeeDTO;
import com.dto.SaveEmployeeDTO;
import com.entity.Employee;
import com.exception.AccountErrorException;
import com.exception.AccountLockErrorException;
import com.exception.PasswordErrorException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mapper.EmployeeMapper;
import com.service.EmployeeService;
import com.vo.PageEmployeeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
   private   EmployeeMapper employeeMapper;
    @Override
    public Employee login(LoginDTO loginDto) {

        Employee employee=new Employee();
        employee=employeeMapper.login(loginDto.getUsername());
        //判断是否存在该用户
        if (employee==null)
        {
           throw  new AccountErrorException(MessageConstant.Account_Error);
        }
        String password = DigestUtils.md5DigestAsHex((loginDto.getPassword()).getBytes());
        //判断密码是否正确
        if (!password.equals(employee.getPassword()))
        {
            throw  new PasswordErrorException(MessageConstant.Passsword_Error);
        }
        //判断账号是否被锁定
        if (employee.getStatus()==0)
        {
            throw  new AccountLockErrorException(MessageConstant.Account_Lock_Error);
        }

        return employee;
    }

    @Override
    public void save(SaveEmployeeDTO saveEmployeeDTO) {
        Employee employee=new Employee();
        BeanUtils.copyProperties(saveEmployeeDTO,employee);
        employee.setStatus(1);
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeMapper.save(employee);

    }

    @Override
    public PageEmployeeVO page(PageEmployeeDTO pageEmployeeDTO) {
        PageEmployeeVO pageEmployeeVO=new PageEmployeeVO();
        PageHelper.startPage((pageEmployeeDTO.getPage()),pageEmployeeDTO.getPageSize());
        Page<Employee> page=employeeMapper.getPage(pageEmployeeDTO.getName());
        pageEmployeeVO.setTotal(page.getTotal());
        pageEmployeeVO.setRecords(page.getResult());
        return pageEmployeeVO;
    }

    @Override
    public void updateStatus(String status, Long id) {
            Employee employee=new Employee();
            employee=  employeeMapper.selectById(id);
            employee.setStatus(Integer.parseInt(status));
            employeeMapper.updateStatus(employee);
    }

    @Override
    public void updateById(Employee employee) {
        employeeMapper.updateById(employee);
    }

    @Override
    public Employee selectById(Long id) {
        Employee employee=new Employee();
        employee= employeeMapper.selectById(id);
       return employee;
    }

    @Override
    public void editPassword(EditPasswordEmployeeDTO editPasswordEmployeeDTO) {
        Employee employee = employeeMapper.selectById(BaseContext.getCurrentId());
        if (!employee.getPassword().equals(DigestUtils.md5DigestAsHex(editPasswordEmployeeDTO.getOldPassword().getBytes())))
        {
            throw new PasswordErrorException(MessageConstant.Passsword_Error);
        }
        String password=DigestUtils.md5DigestAsHex(editPasswordEmployeeDTO.getNewPassword().getBytes());
        editPasswordEmployeeDTO.setEmpId(BaseContext.getCurrentId());
        editPasswordEmployeeDTO.setNewPassword(password);
        employeeMapper.editPassword(editPasswordEmployeeDTO);
    }
}
