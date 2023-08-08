package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.vo.PersonalFollowProjectVO;
import com.atguigu.crowd.entity.vo.PersonalProjectVO;
import com.atguigu.crowd.entity.vo.PersonalPublishProjectVO;
import com.atguigu.crowd.entity.vo.PersonalSupportProjectVO;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.mapper.OrderPOMapper;
import com.atguigu.crowd.mapper.ProjectPOMapper;
import com.atguigu.crowd.service.api.PersonalService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PersonalServiceImpl implements PersonalService {
    @Autowired
    private ProjectPOMapper projectPOMapper;
    @Autowired
    private MemberPOMapper memberPOMapper;
    @Autowired
    private OrderPOMapper orderPOMapper;

    @Override
    public int saveFollowProject(Integer memberId, Integer projectId) {
        try {
            return memberPOMapper.saveFollowProject(memberId, projectId);
        } catch (Exception e) {
            // e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateFollowNum(Integer projectId) {
        return projectPOMapper.updateFollowNum(projectId);
    }

    @Override
    public int delFollowProject(Integer memberId, Integer projectId) {
        return memberPOMapper.delFollowProject(memberId, projectId);
    }

    @Override
    public int updateDelFollowNum(Integer projectId) {
        return projectPOMapper.updateDelFollowNum(projectId);
    }

    @Override
    public PersonalProjectVO selectAllPersonalProject(Integer memberId) throws ParseException {
        PersonalProjectVO personalProjectVO = new PersonalProjectVO();

        List<PersonalFollowProjectVO> personalFollowProjectVOList = projectPOMapper.selectFollowProject(memberId);
        for (PersonalFollowProjectVO personalFollowProjectVO : personalFollowProjectVOList) {
            Date createDate = new SimpleDateFormat("yyyy-MM-dd").parse(personalFollowProjectVO.getCreateDate());
            long pastDays = (new Date().getTime() - createDate.getTime()) / 1000 / 60 / 60 / 24;
            Integer totalDay = personalFollowProjectVO.getDay();
            Integer lastDay = Math.toIntExact((totalDay - pastDays));
            // 设置剩余时间
            personalFollowProjectVO.setLastDay(lastDay);
        }
        List<PersonalSupportProjectVO> personalSupportProjectVOList = projectPOMapper.selectSupportProject(memberId);
        for (PersonalSupportProjectVO personalSupportProjectVO : personalSupportProjectVOList) {
            Date createDate = new SimpleDateFormat("yyyy-MM-dd").parse(personalSupportProjectVO.getCreateDate());
            long pastDays = (new Date().getTime() - createDate.getTime()) / 1000 / 60 / 60 / 24;
            Integer totalDay = personalSupportProjectVO.getDay();
            Integer lastDay = Math.toIntExact((totalDay - pastDays));
            // 设置剩余时间
            personalSupportProjectVO.setLastDay(lastDay);
        }
        List<PersonalPublishProjectVO> personalPublishProjectVOList = projectPOMapper.selectPublishProject(memberId);
        for (PersonalPublishProjectVO personalPublishProjectVO : personalPublishProjectVOList) {
            Date createDate = new SimpleDateFormat("yyyy-MM-dd").parse(personalPublishProjectVO.getCreateDate());
            long pastDays = (new Date().getTime() - createDate.getTime()) / 1000 / 60 / 60 / 24;
            Integer totalDay = personalPublishProjectVO.getDay();
            Integer lastDay = Math.toIntExact((totalDay - pastDays));
            // 设置剩余时间
            personalPublishProjectVO.setLastDay(lastDay);
        }

        personalProjectVO.setMemberId(memberId);
        personalProjectVO.setPersonalFollowProjectVOList(personalFollowProjectVOList);
        personalProjectVO.setPersonalPublishProjectVOList(personalPublishProjectVOList);
        personalProjectVO.setPersonalSupportProjectVOList(personalSupportProjectVOList);
        return personalProjectVO;
    }

    @Override
    public void delOrder(Integer orderId) {
        orderPOMapper.deleteOrderAndReturn(orderId);
    }

    @Override
    public void delProject(Integer projectId) {
        projectPOMapper.updateStatus(projectId);
    }
}
