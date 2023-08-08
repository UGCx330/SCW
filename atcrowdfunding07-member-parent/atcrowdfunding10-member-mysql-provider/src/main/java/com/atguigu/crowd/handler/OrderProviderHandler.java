package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.entity.vo.OrderVO;
import com.atguigu.crowd.service.api.OrderService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class OrderProviderHandler {
    @Autowired
    private OrderService orderService;

    @GetMapping("/get/order/project/vo/remote")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@RequestParam("projectId") Integer projectId,
                                                         @RequestParam("returnId") Integer returnId) {
        try {
            OrderProjectVO orderProjectVO = orderService.getOrderProjectVO(projectId, returnId);
            orderProjectVO.setProjectId(projectId);
            return ResultEntity.successWithData(orderProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @GetMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberId") Integer memberId) {
        try {
            List<AddressVO> addressVOList = orderService.getAddressVOList(memberId);
            return ResultEntity.successWithData(addressVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @PostMapping
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO) {
        try {
            orderService.saveAddress(addressVO);

            return ResultEntity.successWithoutData();

        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }
    }

    @PostMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO,
                                         @RequestParam("memberId") Integer memberId) {
        try {
            orderService.saveOrder(orderVO, memberId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }


}
