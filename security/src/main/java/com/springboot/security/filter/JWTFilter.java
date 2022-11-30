package com.springboot.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @Author:zdd
 * @Date： 2022/11/30 16:33
 */
public class JWTFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ((HttpServletResponse) response).setHeader("token",this.getJwt());
        System.out.println(this.getJwt());
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    //定义token时效和签名
    private long expiration = 60*60*24*1000;
    private String signature  = "admin";

    //生成jwt token
    public String getJwt(){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                //header
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                //payload
                .claim("username","cinco")
                .claim("role","admin")
                .setSubject("admin-test")
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        return jwtToken;
    }

    //从token中取出载荷
    public Claims getPayLoad(String jwtToken){
        Claims claims = Jwts.parser()
                .setSigningKey(signature)
                .parseClaimsJws(jwtToken)
                .getBody();
        return claims;
    }

}
