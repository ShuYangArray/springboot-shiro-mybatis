package com.shu.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    //3.ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro内置过滤器
        /*
         *  anon ：无需认证即可访问
         * authc ：必须认证才能访问
         * user ：必须拥有记住我 功能才能访问
         * perms ： 拥有对某个资源的权限才能访问
         * role ： 拥有某个角色权限才能访问
         * */
        //拦截
        Map<String,String> filterMap = new LinkedHashMap<>();
        /*filterMap.put("/user/add","authc");
        filterMap.put("/user/update","authc");*/
        //授权，正常情况下未授权，会跳到未授权页面
        filterMap.put("/user/add","perms[user:add]");
        filterMap.put("/user/update","perms[user:update]");
        //拦截
        filterMap.put("/user/*","authc");


        bean.setFilterChainDefinitionMap(filterMap);

        //设置登录请求
        bean.setLoginUrl("/tologin");
        //设置未授权的请求
        bean.setUnauthorizedUrl("/noauth");
        return bean;
    }
    //2.DafaultWebSecurityManager
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }
    //1.创建 realm 对象，需要自定义
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
