package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.config.OSSProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectConsumerHandler {
    @Autowired
    private OSSProperties ossProperties;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @PostMapping("/create/project/information")
    public String saveProjectBasicInfo(ProjectVO projectVO, MultipartFile headerPicture,
                                       List<MultipartFile> detailPictureList, Model model,
                                       HttpSession session) throws IOException {
        // 检测头图是否为空
        if (headerPicture.isEmpty()) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }

        // 检测详情图是否为空
        if (detailPictureList == null || detailPictureList.size() == 0) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }

        // 上传头图至阿里云OSS
        ResultEntity<String> headPictureResultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), headerPicture.getInputStream(), ossProperties.getBucketName(), ossProperties.getBucketDomain(), headerPicture.getOriginalFilename());
        if (ResultEntity.FAILED.equals(headPictureResultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }
        projectVO.setHeaderPicturePath(headPictureResultEntity.getData());

        // 上传详情图
        ArrayList<String> path = new ArrayList<>();
        for (MultipartFile detailPicture : detailPictureList) {
            if (detailPicture.isEmpty()) {
                // 检测到详情图片中单个文件为空也是回去显示错误消息
                model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";
            }
            ResultEntity<String> detailPictureResultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), detailPicture.getInputStream(), ossProperties.getBucketName(), ossProperties.getBucketDomain(), detailPicture.getOriginalFilename());
            if (ResultEntity.FAILED.equals(detailPictureResultEntity.getResult())) {
                model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
            path.add(detailPictureResultEntity.getData());
        }
        projectVO.setDetailPicturePathList(path);

        // 众筹基本信息存入zuul的session域
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

        return "redirect:http://123.56.30.35/project/return/info/page";
    }

    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(MultipartFile returnPicture) throws IOException {
        if (returnPicture.isEmpty()) {
            // 检测图片为空也是回去错误消息
            return ResultEntity.failed(CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
        }

        ResultEntity<String> detailPictureResultEntity = CrowdUtil.uploadFileToOss(ossProperties.getEndPoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret(), returnPicture.getInputStream(), ossProperties.getBucketName(), ossProperties.getBucketDomain(), returnPicture.getOriginalFilename());
        return detailPictureResultEntity;
    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturn(ReturnVO returnVO, HttpSession session) {
        try {
            ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
            if (projectVO == null) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
            }

            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            if (returnVOList == null || returnVOList.size() == 0) {
                returnVOList = new ArrayList<>();
                projectVO.setReturnVOList(returnVOList);
            }

            returnVOList.add(returnVO);

            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/create/confirm")
    public String saveConfirm(HttpSession session, MemberConfirmInfoVO memberConfirmInfoVO, Model model) {
        ProjectVO projectVO = (ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        if (projectVO == null) {
            throw new RuntimeException(CrowdConstant.MESSAGE_TEMPLE_PROJECT_MISSING);
        }

        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);

        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberLoginVOId = memberLoginVO.getId();

        ResultEntity<String> saveResultEntity = mySQLRemoteService.saveProjectVORemote(memberLoginVOId, projectVO);
        if (ResultEntity.FAILED.equals(saveResultEntity.getResult())) {
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveResultEntity.getMessage());
            return "project-confirm";
        }

        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);

        return "redirect:http://123.56.30.35/project/create/success";
    }

    @GetMapping("/get/project/detail/{projectId}")
    public String getProjectDetail(@PathVariable("projectId") Integer projectId, Model model) {

        ResultEntity<DetailProjectVO> resultEntity = mySQLRemoteService.getDetailProjectVORemote(projectId);

        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            DetailProjectVO detailProjectVO = resultEntity.getData();

            model.addAttribute("detailProjectVO", detailProjectVO);
        }
        return "project-show-detail";
    }




}
