package com.dto;

import lombok.Data;

@Data
public class SaveEmployeeDTO {
    //用来接受员工新增的相关属性,id  idNumber, name ,phone, sex,username
    private String username;
    private String name;
    private String phone;
    private String sex;
    private String idNumber;
    private Long id;

}
