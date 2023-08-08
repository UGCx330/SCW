package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.MemberConfirmInfoPO;
import com.atguigu.crowd.entity.po.MemberLaunchInfoPO;
import com.atguigu.crowd.entity.po.ProjectPO;
import com.atguigu.crowd.entity.po.ReturnPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.mapper.*;
import com.atguigu.crowd.service.api.ProjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectPOMapper projectPOMapper;
    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;
    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;
    @Autowired
    private ReturnPOMapper returnPOMapper;
    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveProject(Integer memberLoginVOId, ProjectVO projectVO) {
        // 按照表`t_project`存储基本项目信息
        ProjectPO projectPO = new ProjectPO();
        BeanUtils.copyProperties(projectVO, projectPO);
        projectPO.setMemberid(memberLoginVOId);
        String createDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createDate);
        projectPO.setDeploydate(createDate);
        projectPO.setStatus(0);
        projectPOMapper.insertSelective(projectPO);

        Integer projectId = projectPO.getId();

        // 按照关联关系表保存标签分类的关联关系
        List<Integer> typeIdList = projectVO.getTypeIdList();
        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTypeRelationship(projectId, typeIdList);
        projectPOMapper.insertTagRelationship(projectId, tagIdList);

        // 保存项目详情图片到表`t_project_item_pic`
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertPathList(projectId, detailPicturePathList);

        // 保存项目发起人信息到表
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO, memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberLoginVOId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        // 保存回报列表到表`t_return`
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        ArrayList<ReturnPO> returnPOArrayList = new ArrayList<>();
        for (ReturnVO returnVO : returnVOList) {
            ReturnPO returnPO = new ReturnPO();
            BeanUtils.copyProperties(returnVO, returnPO);
            returnPOArrayList.add(returnPO);
        }
        returnPOMapper.insertReturnPOBatch(returnPOArrayList, projectId);

        // 保存项目确认信息到表`t_member_confirm_info`
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO, memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberLoginVOId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);
    }

    // 查询所有分类及其所有项目
    @Override
    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    @Override
    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        // 1.查询得到DetailProjectVO对象
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

        // 2.根据status确定statusText
        Integer status = detailProjectVO.getStatus();
        switch (status) {
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;
            default:
                break;
        }

        // 3.根据deployeDate计算lastDay
        // 2020-10-15
        String deployDate = detailProjectVO.getDeployDate();

        // 获取当前日期
        Date currentDay = new Date();

        // 把众筹日期解析成Date类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployDay = format.parse(deployDate);

            // 获取当前当前日期的时间戳
            long currentTimeStamp = currentDay.getTime();

            // 获取众筹日期的时间戳
            long deployTimeStamp = deployDay.getTime();

            // 两个时间戳相减计算当前已经过去的时间
            long pastDays = (currentTimeStamp - deployTimeStamp) / 1000 / 60 / 60 / 24;

            // 获取总的众筹天数
            Integer totalDays = detailProjectVO.getDay();

            // 使用总的众筹天数减去已经过去的天数得到剩余天数
            Integer lastDay = (int) (totalDays - pastDays);

            detailProjectVO.setLastDay(lastDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return detailProjectVO;
    }

    @Override
    public PageInfo<PortalTypeVO> getPortalProjectDataRemote(Integer typeId, String keyword, Integer pageNum) {
        PageHelper.startPage(pageNum, 8);
        List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalVO(typeId, keyword);
        return new PageInfo<>(portalTypeVOList);
    }

}
