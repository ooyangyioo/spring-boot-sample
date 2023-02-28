package org.yangyi.project.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.yangyi.project.exception.BusinessException;
import org.yangyi.project.mapstruct.SysUserConverter;
import org.yangyi.project.system.dao.SysUserMapper;
import org.yangyi.project.system.dto.UserSignupDTO;
import org.yangyi.project.system.po.SysUser;
import org.yangyi.project.system.po.SysUserRole;
import org.yangyi.project.system.service.ISysUserService;

import java.util.Objects;

@Service
public class SysUserServiceImpl implements ISysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    private SysUserMapper sysUserMapper;
    private SysUserConverter sysUserConverter;

    @Autowired
    public SysUserServiceImpl(SysUserMapper sysUserMapper,
                              SysUserConverter sysUserConverter) {
        this.sysUserMapper = sysUserMapper;
        this.sysUserConverter = sysUserConverter;
    }

    @Override
    public SysUser userSignup(UserSignupDTO userSignupDTO) {
        if (!Objects.isNull(this.sysUserMapper.selectByUserName(userSignupDTO.getUsername())))
            throw new BusinessException("账号已存在");
        SysUser sysUser = sysUserConverter.convert(userSignupDTO);
        int result = sysUserMapper.insert(sysUser);
        return sysUser;
    }

    @Override
    public SysUser userByName(String username) {
        SysUser sysUser = sysUserMapper.selectByUserName(username);
        return sysUser;
    }

    @Override
    @Cacheable(cacheNames = {"user"}, key = "#username")
    public SysUserRole userWithRole(String username) {
        SysUserRole sysUserRole = sysUserMapper.selectUserWithRoles(username);
        return sysUserRole;
    }
}
