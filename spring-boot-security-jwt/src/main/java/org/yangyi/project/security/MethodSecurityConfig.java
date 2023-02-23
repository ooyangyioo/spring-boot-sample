package org.yangyi.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

@Configuration
//  解决@PreAuthorize注解不起作用问题
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

//    /**
//     * 使用 @Secured 检查权限时修改 ROLE_ 前缀, 例如:
//     *
//     * @Secured({AuthorityConstants.ROLE_SYSTEM_ADMIN})
//     */
//    @Override
//    protected AccessDecisionManager accessDecisionManager() {
//        AffirmativeBased accessDecisionManager = (AffirmativeBased) super.accessDecisionManager();
//        setAuthorityRolePrefix(accessDecisionManager, "");
//        return accessDecisionManager;
//    }
//
//    private void setAuthorityRolePrefix(AffirmativeBased accessDecisionManager, String rolePrefix) {
//        accessDecisionManager.getDecisionVoters().stream()
//                .filter(RoleVoter.class::isInstance)
//                .map(RoleVoter.class::cast)
//                .forEach(it -> it.setRolePrefix(rolePrefix));
//    }

    /**
     * 使用 @PreAuthorize 检查权限时修改 ROLE_ 前缀, 例如:
     *
     * @PreAuthorize("hasAnyRole('USER', 'SYSTEM_ADMIN')")
     */
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // 去掉 ROLE_ 前缀
    }
}
