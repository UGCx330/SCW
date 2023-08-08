package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface  ProjectService {
    void saveProject(Integer memberLoginVOId, ProjectVO projectVO);

     List<PortalTypeVO> getPortalTypeVO();

     DetailProjectVO getDetailProjectVO(Integer projectId);

    PageInfo<PortalTypeVO> getPortalProjectDataRemote(Integer typeId, String keyword, Integer pageNum);
}
