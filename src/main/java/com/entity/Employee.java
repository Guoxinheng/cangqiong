package com.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @TableName employee
 */

@Data
public class Employee implements Serializable {
    private Long id;

    private String name;

    private String username;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;
   //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private static final long serialVersionUID = 1L;
}