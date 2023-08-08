package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.PersonalProjectVO;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.service.api.PersonalService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonalProjectProviderHandler {
    @Autowired
    private PersonalService personalService;

    @GetMapping("save/member/follow")
    ResultEntity<String> saveMemberSupport(@RequestParam("memberId") Integer memberId,
                                           @RequestParam("projectId") Integer projectId) {
        // 添加关联表信息
        int i = personalService.saveFollowProject(memberId, projectId);
        if (i == 1) {
            // 关注数+1，关联表异常不会走到这一步
            personalService.updateFollowNum(projectId);
            return ResultEntity.successWithoutData();
        }
        return ResultEntity.failed(CrowdConstant.MESSAGE_CANNOT_FOLLOW);
    }

    @GetMapping("del/member/follow")
    ResultEntity<String> delMemberFollow(@RequestParam("memberId") Integer memberId,
                                         @RequestParam("projectId") Integer projectId) {
        try {
            // 删除关联表信息
            int i = personalService.delFollowProject(memberId, projectId);
            if (i == 1) {
                // 关注数-1，关联表异常不会走到这一步
                personalService.updateDelFollowNum(projectId);
                return ResultEntity.successWithoutData();
            }
            return ResultEntity.failed(CrowdConstant.MESSAGE_CANNOT_DELFOLLOW);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @GetMapping("get/Provider/Project")
    ResultEntity<PersonalProjectVO> getAllPersonalProject(@RequestParam("memberId") Integer memberId) {
        try {
            // 得到所有发布支持关注项目信息
            PersonalProjectVO personalProjectVO = personalService.selectAllPersonalProject(memberId);
            return ResultEntity.successWithData(personalProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    // 删除订单
    @GetMapping("del/order/remote")
    ResultEntity delOrder(@RequestParam("orderId") Integer orderId) {
        try {
            personalService.delOrder(orderId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(CrowdConstant.MESSAGE_CANNOT_DELORDER);
        }
    }

    // 删除项目
    @GetMapping("del/project/remote")
    ResultEntity delProject(@RequestParam("projectId") Integer projectId) {
        try {
            personalService.delProject(projectId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(CrowdConstant.MESSAGE_CANNOT_DELORDER);
        }
    }

}
