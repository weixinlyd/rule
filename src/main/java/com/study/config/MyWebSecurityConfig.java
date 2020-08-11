package com.study.config;

import com.study.filter.AuthenticationTokenFilter;
import com.study.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationTokenFilter authenticationTokenFilter; // token 拦截器

    // 指定密码的加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // 配置用户及其对应的角色   初始化
//    @Override
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService);
//    }

    /**
     * 1.先表单登录
     * 2.在进行权限管理
     * 3.用户访问其它URL都必须认证后访问（登录后访问）
     */
    // 配置 URL 访问权限,登录成功失败返回信息可以不再这里写，可以写成公共的
    protected void configure(HttpSecurity http) throws Exception {
        //        该方法通过数据库获取资源路径
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(accessMustRoles());
                        object.setAccessDecisionManager(rolesCheck());
                        return object;
                    }
                });
        //表单登录功能
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_page")
                .defaultSuccessUrl("/")
                .usernameParameter("name") // 修改认证所需的用户名的参数名（默认为username）
                .passwordParameter("passwd") // 修改认证所需的密码的参数名（默认为password）
                // 定义登录成功的处理逻辑（可以跳转到某一个页面，也可以返会一段 JSON）
                // 定义登录失败的处理逻辑（可以跳转到某一个页面，也可以返会一段 JSON）
                .successHandler(new AdminAuthenticationSuccessHandler())
                .failureHandler(new AdminAuthenticationFailureHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable(); // 关闭csrf
//        token过滤
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
                //这个是注销功能，暂时我没用到，但都是一个套路
//                .and().logout() // 开启注销登录的配置
//                .logoutUrl("/logout") // 配置注销登录请求URL为"/logout"（默认也就是 /logout）
//                .clearAuthentication(true) // 清除身份认证信息
//                .invalidateHttpSession(true) // 使 session 失效
                // 配置一个 LogoutHandler，开发者可以在这里完成一些数据清除工做
//                .addLogoutHandler(new LogoutHandler() {
//                    @Override
//                    public void logout(HttpServletRequest req,
//                                       HttpServletResponse resp,
//                                       Authentication auth) {
//                        System.out.println("注销登录，开始清除Cookie。");
//                    }
//                })
                // 配置一个 LogoutSuccessHandler，开发者可以在这里处理注销成功后的业务逻辑
//                .logoutSuccessHandler(new LogoutSuccessHandler() {
//                    @Override
//                    public void onLogoutSuccess(HttpServletRequest req,
//                                                HttpServletResponse resp,
//                                                Authentication auth)
//                            throws IOException, ServletException {
//                        // 我们可以跳转到登录页面
//                        // resp.sendRedirect("/login");
//
//                        // 也可以返回一段JSON提示
//                        resp.setContentType("application/json;charset=utf-8");
//                        PrintWriter out = resp.getWriter();
//                        resp.setStatus(200);
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("status", 200);
//                        map.put("msg", "注销成功！");
//                        ObjectMapper om = new ObjectMapper();
//                        out.write(om.writeValueAsString(map));
//                        out.flush();
//                        out.close();
//                    }
//                })
        // 用户访问其它URL都必须认证后访问（登录后访问）

    }

    @Bean
    public CustomFilterInvocationSecurityMetadataSource accessMustRoles() {
        return new CustomFilterInvocationSecurityMetadataSource();
    }

    public void withObjectPostProcessor(HttpSecurity http){

    }

    @Bean
    public CustomAccessDecisionManager rolesCheck() {
        return new CustomAccessDecisionManager();
    }

    // 配置角色继承关系
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_DBA > ROLE_ADMIN > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    //解决登录时静态资源的url占用saveRequest保存的之前访问页面地址，跳转不正确的问题
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/templates/**");
    }
}
