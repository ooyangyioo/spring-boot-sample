package org.yangyi.project.service.impl;

import cn.hutool.core.util.RandomUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yangyi.project.domain.SysUser;
import org.yangyi.project.mapper.SysUserMapper;
import org.yangyi.project.service.ISysUserService;

import java.util.Date;

@Service
public class SysUserServiceImpl implements ISysUserService {

    private SysUserMapper sysUserMapper;

    @Autowired
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     */
    @Override
    public String doSignup(String username, String password) {
        String salt = RandomUtil.randomString(16);
        //  MD5
        //  String encryptedPassword = new SimpleHash(Md5Hash.ALGORITHM_NAME, password, ByteSource.Util.bytes(salt), 1024).toBase64();
        //  SHA-256
        String encryptedPassword = new SimpleHash(Sha256Hash.ALGORITHM_NAME, password, ByteSource.Util.bytes(salt), 1024).toBase64();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPasswd(encryptedPassword);
        sysUser.setCreateTime(new Date());
        sysUser.setSalt(salt);
        sysUser.setPhone("15527443931");
        sysUser.setEmail("876359828@qq.com");
        sysUserMapper.insert(sysUser);
        return String.valueOf(sysUser.getUserId());
    }

    @Override
    public SysUser doLogin(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
        return (SysUser) subject.getPrincipal();
    }

}
