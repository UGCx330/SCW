package com.atguigu.crowd.handler;

import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.entity.vo.ProjectVO;
import com.atguigu.crowd.service.api.ProjectService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectProviderHandler {
    @Autowired
    private ProjectService projectService;

    @PostMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestParam("memberLoginVOId") Integer memberLoginVOId, @RequestBody ProjectVO projectVO) {
        try {
            projectService.saveProject(memberLoginVOId, projectVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @GetMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote() {
        try {
            List<PortalTypeVO> portalTypeVOList = projectService.getPortalTypeVO();
            return ResultEntity.successWithData(portalTypeVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @GetMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId) {
        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);
            return ResultEntity.successWithData(detailProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @GetMapping("/getAll/Projects/mysql")
    ResultEntity<PageInfo> getPortalProjectDataRemote(@RequestParam("typeId") Integer typeId,
                                                          @RequestParam("keyword") String keyword,
                                                          @RequestParam("pageNum") Integer pageNum) {
        try {
            PageInfo<PortalTypeVO> pageInfo = projectService.getPortalProjectDataRemote(typeId, keyword, pageNum);
            return ResultEntity.successWithData(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }
}
