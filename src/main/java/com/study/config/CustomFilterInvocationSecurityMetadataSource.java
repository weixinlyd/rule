package com.study.config;

import com.study.entity.Resources;
import com.study.entity.Role;
import com.study.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 当访问一个url时返回这个url所需要的访问权限。
 */
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    // 创建一个AntPathMatcher，主要用来实现ant风格的URL匹配。
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * getAttributes方法返回本次访问需要的权限，可以有多个权限
     * @param o
     * @return在上面的实现中如果没有匹配的url直接返回null，也就是没有配置权限的url默认都为白名单，想要换成默认是黑名单只要修改这里即可。
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // 从参数中提取出当前请求的URL
        String requestUrl = ((FilterInvocation)o).getRequestUrl();
        if ("/login".equals(requestUrl)) {
            System.out.println("登陆无需角色判断");
            //登陆无需角色判断
            return null;
        }
        // 从数据库中获取所有的资源信息，即本案例中的Resources表以及Resources所对应的role
        // 在真实项目环境中，开发者可以将资源信息缓存在Redis或者其他缓存数据库中。
        List<Resources> allResources = resourceMapper.getAllResources();
        List<ConfigAttribute> attributes = new ArrayList<>();
        // 遍历资源信息，遍历过程中获取当前请求的URL所需要的角色信息并返回。
        for(Resources resources:allResources){
            if(antPathMatcher.match(resources.getPattern(),requestUrl)){
                List<Role> roles = resources.getRoles();
                if(CollectionUtils.isEmpty(roles)){
                    throw new IllegalArgumentException();
                }
                for(Role role:roles){
                    SecurityConfig securityConfig = new SecurityConfig(role.getName().trim());
                    attributes.add(securityConfig);
                }
            }else{
                //权限不等则跳过
                continue;
            }
        }
        if(attributes.size()==0){
            return SecurityConfig.createList("ROLE_LOGIN");
        }
        //去重
        List<ConfigAttribute> myList = attributes.stream().distinct().collect(Collectors.toList());
        return myList;
        // 如果当前请求的URL在资源表中不存在相应的模式，就假设该请求登录后即可访问，即直接返回 ROLE_LOGIN.
        //ROLE_LOGIN除了DB权限的访问不了剩下的都可以访问
    }
    // 方法如果返回了所有定义的权限资源，Spring Security会在启动时校验每个ConfigAttribute是否配置正确，不需要校验直接返回null。
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        // 如果不需要校验，那么该方法直接返回null即可。
        return null;
    }
    // supports方法返回类对象是否支持校验。getAttributes方法返回本次访问需要的权限，可以有多个权限
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
