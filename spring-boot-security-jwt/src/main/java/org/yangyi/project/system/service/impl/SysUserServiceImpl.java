package org.yangyi.project.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yangyi.project.exception.BusinessException;
import org.yangyi.project.system.dao.SysUserMapper;
import org.yangyi.project.system.dto.SignupDTO;
import org.yangyi.project.system.po.SysUser;
import org.yangyi.project.system.service.ISysUserService;

import java.util.Objects;

@Service
public class SysUserServiceImpl implements ISysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    private SysUserMapper sysUserMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SysUserServiceImpl(SysUserMapper sysUserMapper,
                              PasswordEncoder passwordEncoder) {
        this.sysUserMapper = sysUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SysUser userSignup(SignupDTO signupDTO) {
        if (!Objects.isNull(this.sysUserMapper.selectByUserName(signupDTO.getUsername())))
            throw new BusinessException("账号已存在");

        SysUser sysUser = new SysUser(){{
            setUserName(signupDTO.getUsername());
            setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        }};
        int result = sysUserMapper.insert(sysUser);

        return sysUser;
    }
}
