package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.AddressPO;
import com.atguigu.crowd.entity.po.AddressPOExample;
import com.atguigu.crowd.entity.po.OrderPO;
import com.atguigu.crowd.entity.po.OrderProjectPO;
import com.atguigu.crowd.entity.vo.AddressVO;
import com.atguigu.crowd.entity.vo.OrderProjectVO;
import com.atguigu.crowd.entity.vo.OrderVO;
import com.atguigu.crowd.mapper.AddressPOMapper;
import com.atguigu.crowd.mapper.OrderPOMapper;
import com.atguigu.crowd.mapper.OrderProjectPOMapper;
import com.atguigu.crowd.mapper.ProjectPOMapper;
import com.atguigu.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;
    @Autowired
    private AddressPOMapper addressPOMapper;
    @Autowired
    private OrderPOMapper orderPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {
        AddressPOExample addressPOExample = new AddressPOExample();
        AddressPOExample.Criteria criteria = addressPOExample.createCriteria();
        criteria.andMemberIdEqualTo(memberId);
        //    数据库查地址按照用户id，然后转VO
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(addressPOExample);
        ArrayList<AddressVO> addressVOArrayList = new ArrayList<>();
        for (AddressPO addressPO : addressPOList) {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO, addressVO);
            addressVOArrayList.add(addressVO);
        }
        return addressVOArrayList;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();

        BeanUtils.copyProperties(addressVO, addressPO);

        addressPOMapper.insert(addressPO);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveOrder(OrderVO orderVO, Integer memberId) {
        OrderPO orderPO = new OrderPO();
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        OrderProjectVO orderProjectVO = orderVO.getOrderProjectVO();
        Integer projectId = orderProjectVO.getProjectId();

        BeanUtils.copyProperties(orderVO, orderPO);
        BeanUtils.copyProperties(orderProjectVO, orderProjectPO);
        // 存订单
        orderPO.setMemberId(memberId);
        orderPOMapper.insert(orderPO);
        Integer orderPOId = orderPO.getId();

        // 存订单项目详情
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        orderProjectPO.setOrderId(orderPOId);
        orderProjectPO.setCreateTime(createTime);
        orderProjectPOMapper.insert(orderProjectPO);

        // 支持人数+1筹集金额++
        double money = (double) ((orderProjectVO.getReturnCount() * orderProjectVO.getSupportPrice()) + orderProjectVO.getFreight());
        projectPOMapper.updateSupportNumAndMoney(projectId, money);

    }

}
