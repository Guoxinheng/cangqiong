package com.constant.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressBookDTO implements Serializable {
    private Long id;//主键
    private  Long userId;//用户id
    private String consignee;//收货人
    private String sex;//性别
    private String phone;//手机号
    private String provinceName;//省级名称
    private String provinceCode;//省级编号
    private String cityCode;//市级编号
    private String cityName;//市级名称
    private String districtCode;//区级编号
    private String districtName;//区级名称
    private  String detail;//详细地址
    private String label;//标签
    private Integer isDefault;//是否默认
}
