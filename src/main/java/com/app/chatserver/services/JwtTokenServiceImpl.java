package com.app.chatserver.services;

import com.app.chatserver.enums.ServerChatErrors;
import com.app.chatserver.exceptions.TokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class JwtTokenServiceImpl implements JwtTokenService {
    @Value("${hay.jwtSecret}")
    private String secret;

    @Override
    public Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
    @Override
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(TextCodec.BASE64.decode(this.secret))
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            throw new TokenException(ServerChatErrors.INVALID_JWT_SIGNATURE.name());
        } catch (MalformedJwtException ex) {
            throw new TokenException(ServerChatErrors.INVALID_JWT_TOKEN.name());
        } catch (ExpiredJwtException ex) {
            throw new TokenException(ServerChatErrors.EXPIRED_TOKEN.name());
        } catch (UnsupportedJwtException ex) {
            throw new TokenException(ServerChatErrors.UNSUPPORTED_JWT_TOKEN.name());
        } catch (IllegalArgumentException ex) {
            throw new TokenException(ServerChatErrors.CLAIMS_IS_EMPTY.name());
        }
    }
}
