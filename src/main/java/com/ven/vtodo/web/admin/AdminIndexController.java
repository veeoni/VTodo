package com.ven.vtodo.web.admin;

import com.ven.vtodo.po.User;
import com.ven.vtodo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminIndexController {
    @Autowired
    private UserService userService;

    @GetMapping//未使用任何参数时，默认全局的路径,当前为/admin
    public String adminPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "admin/index";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String relogin(HttpSession session) {
        return adminPage(session);//加个redirect
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/index";
    }
}
