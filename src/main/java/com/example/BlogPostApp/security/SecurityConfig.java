package com.example.BlogPostApp.security;

import com.example.BlogPostApp.filter.CustomAutenticationFilter;
import com.example.BlogPostApp.filter.CustomAuthorizationFilter;
import com.example.BlogPostApp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserServiceImpl userService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAutenticationFilter customAutenticationFilter=new CustomAutenticationFilter(authenticationManagerBean());
        customAutenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**","/api/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/posts/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/home").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/login").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/css/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/js/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/getUserName/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/favicon.ico").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/favicon.ico").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/post/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/post/comment")
                .hasAnyAuthority("user");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/post/**").hasAnyAuthority("admin");
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/the_post/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/").permitAll();
        http.authorizeRequests().antMatchers("/**").hasAnyAuthority("admin");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAutenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
