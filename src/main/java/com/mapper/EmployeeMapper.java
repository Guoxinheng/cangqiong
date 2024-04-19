package com.mapper;
import com.annotation.AutoFill;
import com.constant.dto.EditPasswordEmployeeDTO;
import com.entity.Employee;
import com.enumeration.OperationType;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;



@Mapper
public interface EmployeeMapper {



    @Select("select * from employee where username=#{username}")
    Employee login(String username);
    @Insert("insert into employee(name,username,password,phone,sex,id_number,create_time,create_user,status,update_time,update_user) values(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{createUser},#{status},#{updateTime},#{updateUser})")
    @AutoFill(value=OperationType.INSERT)
    void save(Employee employee);
    Page<Employee> getPage(@Param("name") String name);
    @Select("select * from Employee where id=#{id}")
    Employee selectById(Long id);
   @Update("update Employee set  status=#{status},update_time=#{updateTime},update_user=#{updateUser} where id=#{id}")
   @AutoFill(value=OperationType.UPDATE)
   void updateStatus(Employee employee) ;
   @Update("update Employee set  name=#{name},username=#{username},phone=#{phone},sex=#{sex},id_number=#{idNumber},update_time=#{updateTime},update_user=#{updateUser} where id=#{id}")
   @AutoFill(value=OperationType.UPDATE)
   void updateById(Employee employee);

   @Update("update Employee set password=#{newPassword},update_time=#{updateTime},update_user=#{updateUser} where id=#{empId}")
   @AutoFill(value=OperationType.UPDATE)
    void editPassword(EditPasswordEmployeeDTO editPasswordEmployeeDTO);
}
