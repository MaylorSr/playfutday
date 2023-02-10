package com.salesianos.triana.playfutday.security.jwt.access;


import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.security.errorhandling.JwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Log
@Service
public class JwtProvider {

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.duration}")
    //private int jwtLifeInDays;
    private int jwtLifeInMinutes;

    private JwtParser jwtParser;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        /*
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());*/
        secretKey = MacProvider.generateKey(SignatureAlgorithm.HS256);


        jwtParser = Jwts.parser()
                .setSigningKey(secretKey);
    }


    public String generateToken(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return generateToken(user);

    }

    public String generateToken(User user) {
        Date tokenExpirationDateTime =
                Date.from(
                        LocalDateTime
                                .now()
                                .plusMinutes(jwtLifeInMinutes)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                );

        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(tokenExpirationDateTime)
                /*
                .signWith(secretKey)*/
                .compact();

    }


    public UUID getUserIdFromJwtToken(String token) {
        return UUID.fromString(
                jwtParser.parseClaimsJws(token).getBody().getSubject()
        );
    }


    public boolean validateToken(String token) {

        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException ex) {
            log.info("Error con el token: " + ex.getMessage());
            throw new JwtTokenException(ex.getMessage());
        }
        //return false;

    }


}
