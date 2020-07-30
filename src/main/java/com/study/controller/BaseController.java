package com.study.controller;

import com.study.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

public class BaseController {
    /**
     * 当前账号常量
     */
    private static final String ACCOUNT = "account";


    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public User getAccount() {
        HttpSession session = getRequest().getSession();
        return (User) session.getAttribute(ACCOUNT);
    }

    public void setAccount(User account) {
        HttpSession session = getRequest().getSession();
        if (account != null) {
            session.setAttribute(ACCOUNT, account);
            //session过期时间设置，以秒为单位，即在没有活动30分钟后，session将失效
            session.setMaxInactiveInterval(30 * 60);
        }
    }
}
