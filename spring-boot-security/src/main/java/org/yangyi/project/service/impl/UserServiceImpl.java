package org.yangyi.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yangyi.project.dao.SysUserDao;
import org.yangyi.project.entity.SysUser;
import org.yangyi.project.service.IUserService;

import java.util.Date;

@Service
public class UserServiceImpl implements IUserService {

    private PasswordEncoder passwordEncoder;
    private SysUserDao sysUserDao;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setSysUserDao(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    @Override
    public SysUser selectUser(Long id) {
        return sysUserDao.selectById(id);
    }

    @Override
    public SysUser selectUser(String username) {
        return sysUserDao.selectByUsername(username);
    }

    @Override
    public Long signup(String username, String password, String phone) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(passwordEncoder.encode(password));
        sysUser.setEmail(username + "@gmail.com");
        sysUser.setPhone(phone);
        sysUser.setCreateTime(new Date());
        sysUserDao.insert(sysUser);
        return sysUser.getUserId();
    }

    @Override
    public boolean userDelete(String userId) {
        int result = sysUserDao.deleteById(Long.valueOf(userId));
        return result != 0;
    }

}
