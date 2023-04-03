package org.yangyi.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

/**
 * 使用hasRole的注解， 方法级别的注解权限需要 ROLE_前缀。
 * 因此，路径拦截权限的名称、注解权限名称、数据库存储的权限名称都要加。
 * 如果数据库的权限名称不加ROLE_前缀，那么在注册权限列表的时候记得拼接ROLE_前缀。
 * 如果不想麻烦，可以使用 hasAuthority ，不需要添加前缀。
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) //  启用@PreAuthorize注解
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    /**
     * 使用 @Secured 检查权限时修改 ROLE_ 前缀, 例如:
     * Secured({AuthorityConstants.ROLE_SYSTEM_ADMIN})
     */
    @Override
    protected AccessDecisionManager accessDecisionManager() {
        AffirmativeBased accessDecisionManager = (AffirmativeBased) super.accessDecisionManager();
        accessDecisionManager.getDecisionVoters().stream()
                .filter(RoleVoter.class::isInstance)
                .map(RoleVoter.class::cast)
                .forEach(it -> it.setRolePrefix(""));
        return accessDecisionManager;
    }

    /**
     * 使用 @PreAuthorize 检查权限时修改 ROLE_ 前缀, 例如:
     * ROLE_前缀需要数据库中就存在或者查询用户的时候加上
     * PreAuthorize("hasAnyRole('USER', 'SYSTEM_ADMIN')")
     */
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // 去掉 ROLE_ 前缀
    }

}
