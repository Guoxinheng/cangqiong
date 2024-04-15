package com.controller.admin;

import com.context.BaseContext;
import com.dto.EditPasswordEmployeeDTO;
import com.dto.LoginDTO;
import com.dto.PageEmployeeDTO;
import com.dto.SaveEmployeeDTO;
import com.entity.Employee;
import com.properties.JwtProperties;
import com.result.Result;
import com.service.EmployeeService;
import com.untils.JwtUtil;
import com.vo.LoginVo;
import com.vo.PageEmployeeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/employee")
@Api(tags = "员工相关接口")
public class EmployeeController {
    @Autowired
  private   EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private  RedisTemplate redisTemplate;
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result login(@RequestBody LoginDTO loginDto)
    {
       Employee employee= employeeService.login(loginDto);
        //生成token
        Map<String,Object> claims =new HashMap<>();
        claims.put("empId",employee.getId());
        String token = jwtUtil.createJWT(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
        LoginVo loginVo=new LoginVo();
        BeanUtils.copyProperties(employee,loginVo);
        loginVo.setToken(token);
        return Result.success(loginVo);
    }
    @PostMapping()
    @ApiOperation("新增员工")
    public  Result save(@RequestBody SaveEmployeeDTO saveEmployeeDTO)
    {
        //进行新增员工
        employeeService.save(saveEmployeeDTO);
        return Result.success();
    }
    @GetMapping("/page")
    public Result<PageEmployeeVO> page(PageEmployeeDTO pageEmployeeDTO)
    {
        PageEmployeeVO pageEmployeeVO=new PageEmployeeVO();
        //分页查询
        pageEmployeeVO=employeeService.page(pageEmployeeDTO);
        return Result.success(pageEmployeeVO);
    }

    @PostMapping("/status/{status}")
    public  Result updateStatus(@PathVariable String status,Long id)
    {
        employeeService.updateStatus(status,id);
        return Result.success();
    }
    @PutMapping()
    public Result<String> update(@RequestBody Employee employee)
    {

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeService.updateById(employee);
        return Result.success("编辑成功");
    }
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id)
    {
        Employee employee=employeeService.selectById(id);
        return Result.success(employee);
    }
    @PutMapping("/editPassword")
    public Result<String> editPassword(@RequestBody EditPasswordEmployeeDTO editPasswordEmployeeDTO)
    {
          employeeService.editPassword(editPasswordEmployeeDTO);
        return Result.success("修改密码成功");
    }
    //TODO 退出
    @PostMapping("/logout")
    public Result<String> logout()
    {
        BaseContext.removeCurrentId();
        return Result.success("退出成功");
    }

}
