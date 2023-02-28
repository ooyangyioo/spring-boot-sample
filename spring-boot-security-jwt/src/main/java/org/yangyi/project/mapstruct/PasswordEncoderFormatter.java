package org.yangyi.project.mapstruct;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Named("PasswordEncoderFormatter")
public class PasswordEncoderFormatter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordEncoderFormatter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //绑定限定符
    @Named("encryption")
    public String format(String data) {
        return passwordEncoder.encode(data);
    }

}
