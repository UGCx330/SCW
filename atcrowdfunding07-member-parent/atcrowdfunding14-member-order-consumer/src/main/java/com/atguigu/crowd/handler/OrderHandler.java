package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.AddressPO;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderHandler {
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId,
            HttpSession session) {

        //含有projectId
        ResultEntity<OrderProjectVO> resultEntity =
                mySQLRemoteService.getOrderProjectVORemote(projectId, returnId);

        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            OrderProjectVO orderProjectVO = resultEntity.getData();

            // 为了能够在后续操作中保持orderProjectVO数据，存入Session域
            session.setAttribute("orderProjectVO", orderProjectVO);
        }
        return "confirm_return";
    }

    @GetMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(
            @PathVariable("returnCount") Integer returnCount,
            HttpSession session) {
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO", orderProjectVO);

        // 3.查询目前的收货地址数据
        MemberLoginVO member = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = member.getId();
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            List<AddressVO> addressVOList = resultEntity.getData();
            session.setAttribute("addressVOList", addressVOList);
        }
        return "confirm_order";
    }

    // 地址表单提交，地址保存到数据库，然后重新调用上面的handler方法，相当于重新点一下去结算按钮
    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {
        // 1.执行地址信息的保存
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);
        // 2.从Session域获取orderProjectVO对象
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();
        // 4.重定向到指定地址，重新进入确认订单页面
        return "redirect:http://123.56.30.35/order/confirm/order/" + returnCount;
    }

}

