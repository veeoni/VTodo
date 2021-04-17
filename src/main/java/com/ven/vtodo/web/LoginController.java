package com.ven.vtodo.web;

import com.ven.vtodo.po.User;
import com.ven.vtodo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping//未使用任何参数时，默认全局的路径,当前为/
    public String home(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user != null){;
            return "redirect:/todo";
        } else {
            return "redirect:/index";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes){
        User user = userService.checkUser(username, password);
        if(user != null){
            user.setPassword(null);//不能把密码传过去，很不安全
            session.setAttribute("user", user);
            return "redirect:/todo";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            //return "admin/login";//不能用这个，否则密码错误重新登录之后路径有问题
            return "redirect:/login";//加个redirect
        }
    }
    @GetMapping("/login")
    public String login(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user != null){;
            return "redirect:/todo";
        } else {
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }
}
