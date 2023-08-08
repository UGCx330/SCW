package com.atguigu.crowd.service.api;

import java.util.List;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Project;
import com.github.pagehelper.PageInfo;

public interface AdminService {

    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String loginAcct, String userPswd);

    Admin getAdminByLoginAcct(String loginAcc);

    PageInfo<Admin> getPageInfo(String keyWord, Integer pageNum, Integer pageSize);

    void remove(Integer adminId);

    Admin getAdminById(Integer adminId);

    void update(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

    PageInfo<Project> getProjectPageInfo(String keyword, Integer pageNum, Integer pageSize);

    void allowProject(Integer projectId);

    void deAllowProject(Integer projectId);
}
