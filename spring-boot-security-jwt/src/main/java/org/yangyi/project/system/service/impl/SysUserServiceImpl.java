package org.yangyi.project.system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yangyi.project.system.dto.SignupDTO;
import org.yangyi.project.system.service.SysUserService;

@Service
public class SysUserServiceImpl implements SysUserService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);


    @Override
    public void userSignup(SignupDTO signupDTO) {

    }
}
