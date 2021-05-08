package com.ven.vtodo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private PermissionInterceptor permissionInterceptor;
    //配置拦截器，
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**", "/todo/**", "/alltodos", "/todos/**", "/waiting")
                .excludePathPatterns("/login", "/register","/admin/roles");
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/admin/roles");
    }
}
