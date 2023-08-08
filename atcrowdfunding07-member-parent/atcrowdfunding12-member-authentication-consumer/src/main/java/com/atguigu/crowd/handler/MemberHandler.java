package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberHandler {
    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @ResponseBody
    @PostMapping("auth/member/send/short/message.json")
    public ResultEntity sendMessage(@RequestParam("phoneNum") String phoneNum) {
        ResultEntity<String> codeResultEntity = CrowdUtil.sendCodeByShortMessage(shortMessageProperties.getPhoneNum(), shortMessageProperties.getSignName(), shortMessageProperties.getTemplateCode());
        if (ResultEntity.SUCCESS.equals(codeResultEntity.getResult())) {
            ResultEntity<String> redisResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(CrowdConstant.REDIS_CODE_PREFIX + phoneNum, codeResultEntity.getData(), 5, TimeUnit.MINUTES);
            if (ResultEntity.SUCCESS.equals(redisResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            }
            return redisResultEntity;
        }
        return codeResultEntity;
    }

    @PostMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, Model model) {
        String phoneNum = memberVO.getPhoneNum();
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
        String code = memberVO.getCode();
        String userpswdBeforeEncoding = memberVO.getUserpswd();

        ResultEntity<String> codeResultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);
        if (ResultEntity.FAILED.equals(codeResultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, codeResultEntity.getMessage());
            return "member-reg";
        }
        if (codeResultEntity.getData() == null) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }
        if (!Objects.equals(codeResultEntity.getData(), code)) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }

        redisRemoteService.removeRedisKeyRemote(key);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String userpasswordAfterEncode = bCryptPasswordEncoder.encode(userpswdBeforeEncoding);
        memberVO.setUserpswd(userpasswordAfterEncode);

        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(memberVO, memberPO);

        ResultEntity<String> mysqlResultEntity = mySQLRemoteService.saveMember(memberPO);
        if (ResultEntity.FAILED.equals(mysqlResultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, mysqlResultEntity.getMessage());
            return "member-reg";
        }

        // 使用重定向避免刷新浏览器导致重新执行注册流程
        return "redirect:http://123.56.30.35/auth/member/to/login/page";
    }

    @PostMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct, @RequestParam("userpswd") String userpswd,
            Model model, HttpSession session) {

        ResultEntity<MemberPO> mysqlResultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        if (ResultEntity.FAILED.equals(mysqlResultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, mysqlResultEntity.getMessage());
            return "member-login";
        }

        MemberPO memberPO = mysqlResultEntity.getData();
        if (memberPO == null) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        String mysqlPassword = memberPO.getUserpswd();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (bCryptPasswordEncoder.matches(userpswd, mysqlPassword)) {
            MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
            session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
            return "redirect:http://123.56.30.35/auth/member/to/center/page";
        }
        model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
        return "member-login";
    }

    @GetMapping("/auth/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:http://123.56.30.35/";
    }
}
