package com.study.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 重写AccessDecisionManager类
 */

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    // 决策当前的访问是否能通过权限验证
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, AuthenticationException {

        Iterator<ConfigAttribute> iterator = collection.iterator();
        // 如果需要的角色是ROLE_LOGIN，说明当前请求的URL用户登录后即可访问
        // 如果auth是UsernamePasswordAuthenticationToken的实例，说明当前用户已登录，该方法到此结束
        while(iterator.hasNext()){
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            ConfigAttribute ca = iterator.next();
            // 否则进入正常的判断流程
            for (GrantedAuthority authority : authorities) {
                // 如果当前用户具备当前请求需要的角色，那么方法结束。
                if (ca.getAttribute().equals(authority.getAuthority())) {
                    return;
                }else {
                    continue;
                }
            }
            if ("ROLE_LOGIN".equals(ca.getAttribute())
                    && authentication instanceof UsernamePasswordAuthenticationToken) {
                throw new BadCredentialsException("权限不足");
            }else{
                continue;
            }
        }
            // 如果不具备权限，就抛出AccessDeniedException异常
            throw new AccessDeniedException("权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
