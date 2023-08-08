package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class PortalHandler {
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @GetMapping("/")
    public String showPortalPage(Model model) {
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA, resultEntity.getData());
            return "portal";
        }
        return "portal";
    }

    @GetMapping("/getAll/Projects/{typeId}")
    public String getAllProjects(Model model, @PathVariable("typeId") Integer typeId,
                                 @RequestParam(value = "keyword",defaultValue = "") String keyword,
                                 @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum) {
        ResultEntity<PageInfo<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalProjectDataRemote(typeId, keyword, pageNum);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_PROJECTS_DATA, resultEntity.getData());
            return "projects";
        }
        return "projects";
    }
}
