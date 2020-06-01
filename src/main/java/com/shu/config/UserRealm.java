package com.shu.config;

import com.shu.pojo.User;
import com.shu.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=》授权doGetAuthorizationInfo");
        principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //授权：add
        //info.addStringPermission("user:add");

        //拿到当前登录对象
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();//拿到user对象
        //设置当前用户权限
        info.addStringPermission(currentUser.getPems());
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了=》认证doGetAuthorizationInfo");
        //用户名密码  数据库中去取

        UsernamePasswordToken usertoken = (UsernamePasswordToken) token;
        User user = userService.queryUserByName(usertoken.getUsername());
        if (user == null){
            return null;
        }

        //密码认证，shiro做
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
