package org.yangyi.project.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwtCredentialsMatcher implements CredentialsMatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtCredentialsMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        LOGGER.info("JwtCredentialsMatcher--->doCredentialsMatch");
        String token = (String) authenticationToken.getCredentials();

//        Object stored = authenticationInfo.getCredentials();
//        String salt = stored.toString();
//        UserDto user = (UserDto)authenticationInfo.getPrincipals().getPrimaryPrincipal();
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(salt);
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withClaim("username", user.getUsername())
//                    .build();
//            verifier.verify(token);
//            return true;
//        } catch (UnsupportedEncodingException | JWTVerificationException e) {
//            log.error("Token Error:{}", e.getMessage());
//        }
        return true;
    }

}
