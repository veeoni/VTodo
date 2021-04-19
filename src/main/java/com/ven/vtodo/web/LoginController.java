package com.ven.vtodo.web;

import com.ven.vtodo.po.User;
import com.ven.vtodo.service.UserService;
import com.ven.vtodo.util.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @GetMapping("/register")
    public String getRegister(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user != null){
            return "redirect:/login";
        }
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String postRegister(User user, HttpSession session){
        User user1 = userService.getUserByUsername(user.getUsername());
        if(user1!=null){
            return "用户名已存在";
        }
        user.setPassword(SHA256Util.getSHA256(user.getUsername()+user.getPassword()));
        System.out.println(user.getAvatar()+" "+user.getUsername());
        User user2 = userService.saveUser(user);
        if(user2!=null){
            user2.setPassword(null);
            session.setAttribute("user", user2);
        }
        return "success";
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
