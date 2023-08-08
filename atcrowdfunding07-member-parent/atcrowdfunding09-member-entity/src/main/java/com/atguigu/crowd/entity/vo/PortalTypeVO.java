package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalTypeVO {

    private Integer id;
    private String name;
    private String remark;
    private String keyword;

    private List<PortalProjectVO> portalProjectVOList;

}
