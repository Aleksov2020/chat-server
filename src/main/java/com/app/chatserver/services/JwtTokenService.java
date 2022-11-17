package com.app.chatserver.services;

import io.jsonwebtoken.Claims;

public interface JwtTokenService {
    /**
     * Get all claims from jwt token
     *
     * @param  token String access token
     * @return All claims from token
     */
    Claims getAllClaimsFromToken(String token);
    /**
     * validate jwt token
     *
     * @param  authToken String access token
     * @return true if access token valid else return false
     */
    boolean validateToken(String authToken);
}
