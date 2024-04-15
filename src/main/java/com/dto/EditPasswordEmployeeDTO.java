package com.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EditPasswordEmployeeDTO {
    Long empId;
    String newPassword;
    String oldPassword;
    LocalDateTime updateTime;
    Long updateUser;
}
