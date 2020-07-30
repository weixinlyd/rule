package com.study.controller;

import com.study.entity.User;
import com.study.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/hello")
    public String admin() {
        return "hello admin";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }
    @GetMapping("/db/hello")
    public String db() {
        return "hello db";
    }
    @GetMapping("/login")
    public String hello(){
        return "login_page";
    }

    @GetMapping("hello")
    public String login(){
        return "hello";
    }

    @PostMapping("/login_page")
    public String addLogin(@RequestBody User user){
        UserDetails user1 = userService.loadUserByUsername(user.getUsername());
        boolean isTrue = passwordEncoder.matches(user.getPassword(),user1.getPassword());
        if(isTrue=true){
            return "success";
        }else{
            return "wrong";
        }
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
//    @PreAuthorize("hasAnyRole('admin')")
    public String helloAdmin() {
        return "admin";
    }

    /**
     * Spring Security获取已登录的用户信息
     * @return
     */
    @RequestMapping(value = "/admin/user", method = RequestMethod.GET)
    public ResponseEntity<Object> getUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        return new ResponseEntity(principal, HttpStatus.OK);
    }
}