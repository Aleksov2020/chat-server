package com.app.chatserver.services;

import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.UserException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class WebSocketAuthenticatorService {
    // This method MUST return a UsernamePasswordAuthenticationToken instance, the spring security chain is testing it with 'instanceof' later on. So don't use a subclass of it or any other class
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    public WebSocketAuthenticatorService(JwtTokenService jwtTokenService, UserService userService) {
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String accessToken) throws AuthenticationException {
        // validate jwt access token
        jwtTokenService.validateToken(accessToken);

        //checkUserExists
        if ( userService.checkUserExistsById((Integer) jwtTokenService.getAllClaimsFromToken(accessToken).get("id")) ) {
            // null credentials, we do not pass the password along
            return new UsernamePasswordAuthenticationToken(
                    jwtTokenService.getAllClaimsFromToken(accessToken).get("id").toString(),
                    null,
                    Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
            );
        } else {
            throw new UserException(ServerChatErrors.USER_NOT_FOUND.name());
        }


    }
}
