package com.study.rule;

import com.study.config.CustomFilterInvocationSecurityMetadataSource;
import com.study.entity.User;
import com.study.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class RuleApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Test
    void contextLoads() {
    }
    //测试数据加密
    @Test
    void test1() {
        User user = (User) userService.loadUserByUsername("admin");
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<查询数据库成功>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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

    //redis测试
    @Test
    public void test2() {
        String key = "user";
        String value = "tom";
        redisTemplate.opsForValue().set(key,value);
        System.out.println(redisTemplate);
        System.out.println("成功");
    }

}
