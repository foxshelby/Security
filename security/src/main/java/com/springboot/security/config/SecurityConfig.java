package com.springboot.security.config;

import com.springboot.security.authentic.CustomeAuthenticationManager;
import com.springboot.security.authentic.CustomeAuthenticationProvider;
import com.springboot.security.handler.CustomeFailureHandler;
import com.springboot.security.handler.CustomeSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Author:zdd
 * @Date： 2022/11/30 15:22
 */
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //启用CSRF保护  默认就会开启可以使用disabled()禁用
                .csrf()
                .and()
                //开启表单登录的方式  会将 UsernamePasswordAuthenticationFilter 加入到过滤链中
                .formLogin()
                //下面指定了登录页的位置、失败/成功的重定向页面位置、表单登录时接收的用户名和密码
                .loginPage("/login")
                .failureForwardUrl("/error")
                .successForwardUrl("/success")
                .passwordParameter("password")
                .usernameParameter("username")
                //对失败/成功的请求进行处理
                .failureHandler(new CustomeFailureHandler())
                .successHandler(new CustomeSuccessHandler())
                .and()
                //进行请求的特定处理
                .authorizeRequests()
                //对于请求路径是/api开头的请求,只有拥有USER权限的用户才能访问
                .antMatchers("/api/**").hasRole("USER")
                //对于admin的请求，全部放行
                .antMatchers("/admin/**").permitAll()
                //对于cal开头的请求 只有授权的用户才能访问
                .antMatchers("/cal/**").authenticated()
                //通过表达式判断， 只有通过表达式的请求才能正常访问
                .antMatchers("/user/**").access("hasRole('ROLE_USER')")
                .and()
                //添加自己的身份验证处理器，只要provider有一个通过身份验证即可访问
                .authenticationProvider(new CustomeAuthenticationProvider())
                .authenticationManager(new CustomeAuthenticationManager())
                //指定在特定过滤器位置 添加新的过滤器   JWT 的设置就是在这里设置的
//                .addFilterAfter()
//                .addFilterAfter()
                .build();
    }
}
