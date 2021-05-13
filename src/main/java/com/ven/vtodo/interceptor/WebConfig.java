package com.ven.vtodo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private PermissionInterceptor permissionInterceptor;
    @Autowired
    private TodoInterceptor todoInterceptor;
    @Autowired
    private TagInterceptor tagInterceptor;
    @Autowired
    private TypeInterceptor typeInterceptor;
    @Autowired
    private CountdownInterceptor countdownInterceptor;
    @Autowired
    private InfoInterceptor infoInterceptor;
    @Autowired
    private NoteInterceptor noteInterceptor;
    @Autowired
    private TargetInterceptor targetInterceptor;
    //配置拦截器，
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin","/admin/login","/waiting");
        registry.addInterceptor(permissionInterceptor).addPathPatterns("/admin/roles", "/admin/roles/**",
                "/admin/users", "/admin/users/**", "/admin/setroles", "/admin/setroles/**");
        registry.addInterceptor(todoInterceptor).addPathPatterns("/todo","/todos","/todo/**","/todos/**","/alltodos");
        registry.addInterceptor(tagInterceptor).addPathPatterns("/admin/tags","/admin/tags/**");
        registry.addInterceptor(typeInterceptor).addPathPatterns("/admin/types","/admin/types/**");
        registry.addInterceptor(targetInterceptor).addPathPatterns("/admin/targets","/admin/targets/**");
        registry.addInterceptor(infoInterceptor).addPathPatterns("/admin/info","/admin/passchange");
        registry.addInterceptor(noteInterceptor).addPathPatterns("/admin/notes","/admin/notes/**");
        registry.addInterceptor(countdownInterceptor).addPathPatterns("/admin/countdowns","/admin/countdowns/**");
    }
}
