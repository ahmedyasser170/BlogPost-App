package com.example.BlogPostApp.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.BlogPostApp.model.Role;
import com.example.BlogPostApp.model.User;
import com.example.BlogPostApp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

public class JwtUtility {
    @Autowired
    static UserServiceImpl userService;
    public static Algorithm generateAlgorithm() {
        Algorithm algorithm=Algorithm.HMAC256("secret".getBytes());
        return algorithm;
    }
    public static String generateAccessToken(User user, HttpServletRequest request) {
        Algorithm algorithm=JwtUtility.generateAlgorithm();
        String access_token= JWT.create()
                .withSubject(user.getUserName())
                .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;
    }
    public static String generateAccessToken(org.springframework.security.core.userdetails.User user, HttpServletRequest request) {
        Algorithm algorithm=JwtUtility.generateAlgorithm();
        String access_token= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return access_token;
    }

    public static String generateRefreshToken(org.springframework.security.core.userdetails.User user, HttpServletRequest request) {
        Algorithm algorithm=JwtUtility.generateAlgorithm();
        String refresh_token= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+60*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        return refresh_token;
    }
    public static DecodedJWT generateDecodeJwt(Algorithm algorithm,String token) {
        JWTVerifier jwtVerifier= JWT.require(algorithm).build();
        DecodedJWT decodedJWT=jwtVerifier.verify(token);
        return decodedJWT;
    }
    public static HashMap<String,String> generateMap(String refresh_token,String access_token) {
        HashMap<String,String> map = new HashMap<>();
        map.put("access_token",access_token);
        map.put("refresh_token",refresh_token);
        return map;
    }
    public static String generateAccessToken() {
        String token="";
        return token;
    }
    public static String[] getRules(String token) {
        Algorithm algorithm=JwtUtility.generateAlgorithm();
        DecodedJWT decodedJWT=JwtUtility.generateDecodeJwt(algorithm,token);
        return decodedJWT.getClaim("roles").asArray(String.class);
    }

    public static String getUserName(String token) {
        Algorithm algorithm=JwtUtility.generateAlgorithm();
        DecodedJWT decodedJWT=JwtUtility.generateDecodeJwt(algorithm,token);
        return decodedJWT.getSubject();
    }
}
