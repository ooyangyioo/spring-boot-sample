package org.yangyi.project.mapstruct;

import org.mapstruct.*;
import org.yangyi.project.system.dto.UserSignupDTO;
import org.yangyi.project.system.po.SysUser;

/**
 * InjectionStrategy.CONSTRUCTOR 基于构造器注入
 */
@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {PasswordEncoderFormatter.class})
public interface SysUserConverter {
    @Mappings({
            @Mapping(source = "username", target = "userName"),
            @Mapping(target = "password", qualifiedByName = "encryption"),
            @Mapping(target = "nickName", source = "username"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "phoneNumber", source = "phone")
    })
    SysUser convert(UserSignupDTO userSignupDTO);
}