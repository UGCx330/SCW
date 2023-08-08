package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalSupportProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer orderId;
    // 交易状态：暂时为1，交易关闭
    private Integer payStatus = 1;

    // 项目名称--项目表
    private String projectName;
    // supportmoney / money * 100 percentage,  完成度 项目表
    private Long supportMoney;
    private Long money;
    private Integer percentage;
    // 剩余天数--总发布天数-（现在日期-创建日期）--项目表
    private Integer day;
    private String createDate;
    private Integer lastDay;


    // 订单创建时间---支持日期--订单项目表
    private String createTime;
    // 支持金额--订单项目表
    private Integer supportPrice;
    // 运费--订单项目表
    private Integer freight;
    // 回报数量--订单项目表
    private Integer returnCount;

    //订单号--订单表
    private String orderNum;

    //是否被删除
    private Integer status;
}
