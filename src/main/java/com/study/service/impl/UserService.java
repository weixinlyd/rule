package com.study.service.impl;

import com.study.Utils.JwtUtils;
import com.study.entity.Role;
import com.study.entity.User;
import com.study.entity.UserToken;
import com.study.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserService implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    //注册时使用passwordEncoder.encode进行加密
    //然后在登录时通过passwordEncoder.match进行解密,获取前端提供的password以及数据库保存的盐值
    //登录加载角色信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.loadUserByUsername(username);
        // 数据库密码加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        //生成token
        String token = null;
        System.out.println("加密后的密码：" + encodePassword);
        user.setPassword(encodePassword);
        user.setToken(token);
        //其实应该保存到redis比较好吧 token
        if (user == null) {
            throw new UsernameNotFoundException("账户不存在!");
        }
        List<Role> userRoles = userMapper.getUserRolesByUid(user.getId());
        user.setUserRoles(userRoles);
        return user;
    }

    public User findByUserName(String username){
        return userMapper.loadUserByUsername(username);
    }

}



//System.out.println("加密后的密码：" + user.getPassword());
//        user.setPassword(user.getPassword());
//        if (user == null) {
//        throw new UsernameNotFoundException("账户不存在!");
//        }
//        String password = "123456";
//        boolean isTrue = passwordEncoder.matches(password,user.getPassword());
//        if(!isTrue){
//        throw new UsernameNotFoundException("密码未匹配上!");
//        }
//        System.out.println("匹配成功");
