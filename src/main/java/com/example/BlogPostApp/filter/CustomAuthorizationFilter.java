package com.example.BlogPostApp.filter;

import com.example.BlogPostApp.utils.JwtUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getServletPath());
        if(request.getServletPath().equals("/api/login") ||
                request.getServletPath().equals("/api/token/refresh"))
            filterChain.doFilter(request,response);
        else {
            String authoriztionHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
            log.info("auth is {} ",authoriztionHeader);
            if(authoriztionHeader!=null && authoriztionHeader.startsWith("Bearer ")) {
                try {
                    String token=authoriztionHeader.substring("Bearer ".length());
                    String userName=JwtUtility.getUserName(token);
                    String[] rules=JwtUtility.getRules(token);
                    Collection<SimpleGrantedAuthority> collection=new ArrayList<>();
                    Arrays.stream(rules).forEach(rule->{
                        collection.add(new SimpleGrantedAuthority(rule));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken=
                            new UsernamePasswordAuthenticationToken(userName,null,collection);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                } catch (Exception e) {
                    log.error("error is {} ",e.getMessage());
                    response.setHeader("error",e.getMessage());
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    Map<String,String> map=new HashMap<>();
                    map.put("error",e.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),map);
                }
            } else {
                filterChain.doFilter(request,response);
            }

        }
    }
}
