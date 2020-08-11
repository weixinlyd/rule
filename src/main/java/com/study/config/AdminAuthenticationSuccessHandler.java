package com.study.config;

import com.study.Utils.JwtUtils;
import com.study.Utils.RedisUtil;
import com.study.Utils.ResponseUtils;
import com.study.entity.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//    @Resource
//    private RedisTemplate redisTemplate;
//    @Autowired
//    private RedisUtil redisUtil;
    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        User user = (User) auth.getPrincipal();
        Map map = new HashMap();
        try {
            //过期时间
            Integer time = 1000*60*60;
            user.setToken(JwtUtils.generateToken(user,time));
            //2.将token添加到缓存中
//            redisTemplate.opsForValue().set("a",user.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            map.put("message","登录失败,添加缓存异常 ");
            map.put("entity",user);
            map.put("status",400);
            ResponseUtils.write(response,map);
        }
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