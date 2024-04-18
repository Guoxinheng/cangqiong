package com.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserReportVO implements Serializable {

    private String  dateList;
    private String  newUserList;
    private String  totalUserList;

}
