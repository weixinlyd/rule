package com.study.config;

import com.study.Utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>();
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            map.put("msg","账户名或密码输入错误，登录失败!");
        } else if (e instanceof LockedException) {
            map.put("msg","账户被锁定，请联系管理员!");
        } else if (e instanceof CredentialsExpiredException) {
            map.put("msg","证书过期，请联系管理员!");
        } else if (e instanceof AccountExpiredException) {
            map.put("msg","账户过期，请联系管理员!");
        } else if (e instanceof DisabledException) {
            map.put("msg","账户被禁用，请联系管理员!");
        } else {
            map.put("msg","登录失败");
        }
        map.put("status","400");
        try {
            httpServletResponse.setStatus(400);
            ResponseUtils.write(httpServletResponse,map);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
