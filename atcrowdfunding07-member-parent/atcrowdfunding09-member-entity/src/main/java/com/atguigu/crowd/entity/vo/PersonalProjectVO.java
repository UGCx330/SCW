package com.atguigu.crowd.entity.vo;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalProjectVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    // 用户id
    private Integer memberId;
    // 我支持的所有项目
    private List<PersonalSupportProjectVO> personalSupportProjectVOList;
    // 我关注的所有项目
    private List<PersonalFollowProjectVO> personalFollowProjectVOList;
    // 我发起的所有项目
    private List<PersonalPublishProjectVO> personalPublishProjectVOList;
}
