package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.PersonalProjectVO;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PersonalProjectHandler {
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @ResponseBody
    // 关注，响应前端ajax
    @PostMapping("/member/follow")
    public ResultEntity<String> saveMemberSupportProject(@RequestParam("memberId") Integer memberId,
                                                         @RequestParam("projectId") Integer projectId) {
        // 添加用户-关注项目表信息,同时项目表的关注数+1
        ResultEntity<String> resultEntity = mySQLRemoteService.saveMemberFollow(memberId, projectId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            return ResultEntity.successWithoutData();
        }
        return ResultEntity.failed(CrowdConstant.MESSAGE_CANNOT_FOLLOW);
    }

    @ResponseBody
    // 取消关注，响应前端ajax
    @PostMapping("/member/delfollow")
    public ResultEntity<String> delMemberSupportProject(@RequestParam("memberId") Integer memberId,
                                                        @RequestParam("projectId") Integer projectId) {
        // 删除用户-关注项目表信息,同时项目表的关注数-1
        return mySQLRemoteService.delMemberFollow(memberId, projectId);
    }


    // 一次性得到个人所有支持的，发布的，关注的项目
    @GetMapping("/member/my/crowd")
    public String getProviderProject(HttpSession session, Model model) {
        // session得到登录者id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();
        // 得到支持的，关注的，发起的项目列表,存储进PersonalProjectVO
        ResultEntity<PersonalProjectVO> resultEntity = mySQLRemoteService.getAllPersonalProject(memberId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_PERSONAL_ALL_PROJECT, resultEntity.getData());
            return "member-crowd";
        }
        return "member-crowd";
    }

    // 删除订单
    @GetMapping("del/order")
    @ResponseBody
    public ResultEntity delOrder(@RequestParam("orderId") Integer orderId) {
        ResultEntity resultEntity = mySQLRemoteService.delOrder(orderId);
        return resultEntity;
    }

    // 删除项目--status置为3
    @GetMapping("del/project")
    @ResponseBody
    public ResultEntity delProject(@RequestParam("projectId") Integer projectId) {
        ResultEntity resultEntity= mySQLRemoteService.delProject(projectId);
        return resultEntity;
    }
}
