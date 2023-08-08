package com.atguigu.crowd.mapper;


import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.po.ProjectPOExample;
import com.atguigu.crowd.entity.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectPOMapper {
    int countByExample(ProjectPOExample example);

    int deleteByExample(ProjectPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProjectPO record);

    int insertSelective(ProjectPO record);

    List<ProjectPO> selectByExample(ProjectPOExample example);

    ProjectPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByExample(@Param("record") ProjectPO record, @Param("example") ProjectPOExample example);

    int updateByPrimaryKeySelective(ProjectPO record);

    int updateByPrimaryKey(ProjectPO record);

    void insertTypeRelationship(@Param("projectId") Integer projectId, @Param("typeIdList") List<Integer> typeIdList);

    void insertTagRelationship(@Param("projectId") Integer projectId, @Param("tagIdList") List<Integer> tagIdList);

    List<PortalTypeVO> selectPortalTypeVOList();

    DetailProjectVO selectDetailProjectVO(Integer projectId);

    List<PortalTypeVO> selectPortalVO(@Param("typeId") Integer typeId, @Param("keyword") String keyword);

    void updateSupportNumAndMoney(@Param("projectId") Integer projectId, @Param("money") double money);

    int updateFollowNum(@Param("projectId") Integer projectId);

    int updateDelFollowNum(@Param("projectId") Integer projectId);

    List<PersonalFollowProjectVO> selectFollowProject(@Param("memberId") Integer memberId);

    List<PersonalSupportProjectVO> selectSupportProject(@Param("memberId") Integer memberId);

    List<PersonalPublishProjectVO> selectPublishProject(@Param("memberId") Integer memberId);

    void updateStatus(@Param("projectId") Integer projectId);
}