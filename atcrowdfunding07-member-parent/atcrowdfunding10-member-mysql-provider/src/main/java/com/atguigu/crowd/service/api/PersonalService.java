package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.vo.PersonalProjectVO;

import java.text.ParseException;

public interface PersonalService {
    int saveFollowProject(Integer memberId, Integer projectId);

    int updateFollowNum(Integer projectId);

    int delFollowProject(Integer memberId, Integer projectId);

    int updateDelFollowNum(Integer projectId);

    PersonalProjectVO selectAllPersonalProject(Integer memberId) throws ParseException;

    void delOrder(Integer orderId);

    void delProject(Integer projectId);
}
