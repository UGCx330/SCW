package com.atguigu.crowd.api;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("zzh-crowd-mysql")
public interface MySQLRemoteService {
    @GetMapping("/get/memberpo/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct);

    @PostMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);

    @PostMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestParam("memberLoginVOId") Integer memberLoginVOId, @RequestBody ProjectVO projectVO);

    @GetMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

    @GetMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);

    @GetMapping("/get/order/project/vo/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(
            @RequestParam("projectId") Integer projectId,
            @RequestParam("returnId") Integer returnId);

    @GetMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId);

    @PostMapping
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);

    @PostMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO,
                                         @RequestParam("memberId") Integer memberId);

    @GetMapping("/getAll/Projects/mysql")
    ResultEntity<PageInfo<PortalTypeVO>> getPortalProjectDataRemote(@RequestParam("typeId") Integer typeId,
                                                                    @RequestParam("keyword") String keyword,
                                                                    @RequestParam("pageNum") Integer pageNum);

    @GetMapping("get/Provider/Project")
    ResultEntity<PersonalProjectVO> getAllPersonalProject(@RequestParam("memberId") Integer memberId);

    @GetMapping("save/member/follow")
    ResultEntity<String> saveMemberFollow(@RequestParam("memberId") Integer memberId,
                                          @RequestParam("projectId") Integer projectId);

    @GetMapping("del/member/follow")
    ResultEntity<String> delMemberFollow(@RequestParam("memberId") Integer memberId,
                                         @RequestParam("projectId") Integer projectId);

    @GetMapping("del/order/remote")
    ResultEntity delOrder(@RequestParam("orderId") Integer orderId);

    @GetMapping("del/project/remote")
    ResultEntity delProject(@RequestParam("projectId") Integer projectId);
}
