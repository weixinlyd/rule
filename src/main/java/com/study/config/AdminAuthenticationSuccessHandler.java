package com.study.config;

import com.study.Utils.JwtUtils;
import com.study.Utils.ResponseUtils;
import com.study.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        User user = (User) auth.getPrincipal();
        try {
            //过期时间
            Integer time = 1000*60*60;
            user.setToken(JwtUtils.generateToken(user,time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        map.put("message","登录成功");
        map.put("entity",user);
        map.put("status",200);
        map.put("token",user.getToken());
        try {
            response.setStatus(200);
            ResponseUtils.write(response,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}