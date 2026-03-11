package com.microservicios.auth_server.helpers;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtHelper {
@Value("${application.jwt.secret}")
private String jwtSecret;


public String createToken(String username){

    final var now = new Date();
    var expirationDate = new Date(now.getTime() + (3600 * 1000));
    return Jwts
            .builder()
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(expirationDate)
            .signWith(this.getKey())
            .compact();
}


public boolean validateToken(String token){
    try {
        final var expirationDate= this.getExpirationDate(token);

        return expirationDate.after(new Date());
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"JWT Invalid");
    }
}



private Date getExpirationDate(String token){
    return this.getClaimsFromToken(token, Claims::getExpiration);
}

private <T> T getClaimsFromToken(String token, Function<Claims, T> resolver){
    return resolver.apply(signToken(token));
}

private Claims signToken( String token){
    return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
}

private SecretKey getKey(){
    return Keys.hmacShaKeyFor(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
}

}
