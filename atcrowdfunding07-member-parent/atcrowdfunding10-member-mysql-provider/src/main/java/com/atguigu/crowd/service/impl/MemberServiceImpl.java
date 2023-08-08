package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberPOMapper memberPOMapper;


    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {
        MemberPOExample memberPOExample = new MemberPOExample();
        MemberPOExample.Criteria criteria = memberPOExample.createCriteria();
        criteria.andLoginacctEqualTo(loginacct);
        List<MemberPO> memberPOList = memberPOMapper.selectByExample(memberPOExample);
        if (memberPOList == null || memberPOList.size() == 0) {
            return null;
        }
        return memberPOList.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class, readOnly = false)
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
