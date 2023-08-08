package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalPublishProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer projectId;

    // 项目名称--项目表
    private String projectName;

    //已经筹集的金额--项目表
    private Long money;

    // supportmoney / money * 100 percentage,  完成度 项目表
    private Long supportMoney;
    private Integer percentage;

    // 剩余天数--总发布天数-（现在日期-创建日期）--项目表
    private Integer day;
    private String createDate;
    private Integer lastDay;

    //当前状态--项目表，1审核通过，0未通过
    private Integer status;
}
