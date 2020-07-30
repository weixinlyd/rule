package com.study.filter;

import com.study.Utils.JwtUtils;
import com.study.entity.UserToken;
import com.study.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {


    @Autowired
    private UserService userService; //用户信息service

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        // 从参数中提取出当前请求的URL
//        String requestUrl = ((FilterInvocation)request).getRequestUrl();
//        if ("/login".equals(requestUrl)) {
//            System.out.println("登陆无需角色判断");
//            //登陆无需角色判断
//            return null;
//        }
        UserToken userToken = new UserToken();
        String authHeader = request.getHeader("token");//获取header中的验证信息
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring("Bearer ".length());

            try {
                userToken = JwtUtils.getInfoFromToken(authToken); //从token中获取用户信息，jwtUtils自定义的token加解密方式
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (userToken.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(userToken.getUsername());//根据用户名获取用户对象

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //设置为已登录
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}