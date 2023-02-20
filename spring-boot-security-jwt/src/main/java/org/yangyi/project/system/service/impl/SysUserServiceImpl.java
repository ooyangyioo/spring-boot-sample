package org.yangyi.project.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yangyi.project.system.dao.SysUserMapper;
import org.yangyi.project.system.dto.SignupDTO;
import org.yangyi.project.system.po.SysUser;
import org.yangyi.project.system.service.SysUserService;

@Service
public class SysUserServiceImpl implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    private SysUserMapper sysUserMapper;

    @Autowired
    public SysUserServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public SysUser userSignup(SignupDTO signupDTO) {
        SysUser sysUser = this.sysUserMapper.selectByUserName("admin");
        return sysUser;
    }
}
