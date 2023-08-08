package com.atguigu.crowd.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Project;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;

import javax.security.auth.login.LoginException;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveAdmin(Admin admin) {
        String userPswd = admin.getUserPswd();
        String md5 = passwordEncoder.encode(userPswd);
        admin.setUserPswd(md5);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String creatTime = simpleDateFormat.format(date);
        admin.setCreateTime(creatTime);


        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginAcct);
        List<Admin> adminList = adminMapper.selectByExample(adminExample);
        if (adminList == null || adminList.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (adminList.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin = adminList.get(0);
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        String pwdFromFrom = CrowdUtil.md5(userPswd);
        String pwdFromDB = admin.getUserPswd();

        if (!Objects.equals(pwdFromDB, pwdFromFrom)) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        return admin;
        //    4297F44B13955235245B2497399D7A93
        //    4297F44B13955235245B2497399D7A93
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {

        AdminExample example = new AdminExample();

        AdminExample.Criteria criteria = example.createCriteria();

        criteria.andLoginAcctEqualTo(username);

        List<Admin> list = adminMapper.selectByExample(example);

        Admin admin = list.get(0);

        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyWord, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> adminList = adminMapper.selectAdminByKeyword(keyWord);
        return new PageInfo<>(adminList);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        adminMapper.deleteOLdRelationship(adminId);
        if (roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }

    @Override
    public PageInfo<Project> getProjectPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Project> projectList = adminMapper.getProject(keyword);
        return new PageInfo<>(projectList);
    }

    @Override
    public void allowProject(Integer projectId) {
        adminMapper.allowProject(projectId);
    }

    @Override
    public void deAllowProject(Integer projectId) {
        adminMapper.deAllowProject(projectId);
    }

}
