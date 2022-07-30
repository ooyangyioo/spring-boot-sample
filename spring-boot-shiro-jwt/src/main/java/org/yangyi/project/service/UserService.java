package org.yangyi.project.service;

import cn.hutool.core.lang.Snowflake;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yangyi.project.dao.ShiroUserDao;
import org.yangyi.project.entity.ShiroUser;
import org.yangyi.project.shiro.UserAuthorizingRealm;

import java.util.Date;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final ShiroUserDao shiroUserDao;
    private final Snowflake snowflake;

    @Autowired(required = false)
    public UserService(ShiroUserDao shiroUserDao) {
        this.shiroUserDao = shiroUserDao;

        snowflake = new Snowflake(1, 1);
    }

    public ShiroUser getUserInfo(String username) {
        ShiroUser sysUser = shiroUserDao.selectByUsername(username);
        return sysUser;
    }

    public Long userRegister(String username, String password) {
        SimpleHash simpleHash = new SimpleHash(Sha256Hash.ALGORITHM_NAME, password, UserAuthorizingRealm.ENCRYPT_SALT, 1024);
        //  simpleHash = new SimpleHash(Md5Hash.ALGORITHM_NAME, password, "test", 1024);

        Long userId = snowflake.nextId();
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setId(userId);
        shiroUser.setUsername(username);
        shiroUser.setPassword(simpleHash.toHex());
        shiroUser.setPhone("15527443931");
        shiroUser.setEmail("876359828@qq.com");
        shiroUser.setCreateTime(new Date());
        int result = shiroUserDao.insertSelective(shiroUser);
        if (result == 0)
            return null;
        return userId;
    }

}
