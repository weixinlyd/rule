package com.study.rule;

import com.study.Utils.JwtUtils;
import com.study.config.CustomFilterInvocationSecurityMetadataSource;
import com.study.entity.User;
import com.study.entity.UserToken;
import com.study.service.impl.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class RuleApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Test
    void contextLoads() {
    }
    //测试数据加密
    @Test
    void test1() {
        User user = (User) userService.loadUserByUsername("aaa");
        System.out.println(user.getPassword());
        System.out.println("查询数据库成功,加密成功");
    }
//    @Test
//    public void test2() throws Exception {
//        UserToken userToken = new UserToken();
//        userToken.setUsername("admin");
//        userToken.setUserId(1);
//        String value = JwtUtils.generateToken(userToken,2);
//        System.out.println(value);
//        System.out.println("查询数据库成功,加密成功");
//    }

    //测试配置，通过url来获取角色

}
