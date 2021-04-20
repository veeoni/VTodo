package com.ven.vtodo.web.admin;

import com.ven.vtodo.po.User;
import com.ven.vtodo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class InfoController {
    @Autowired
    private UserService userService;


    @GetMapping("/info")//未使用任何参数时，默认全局的路径,当前为/admin
    public String infoPage(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user != null){;
            User user2 = userService.getUserById(user.getId());
            user2.setPassword(null);
            session.setAttribute("user", user2);
            model.addAttribute("user", user2);
            return "admin/info";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/info")
    public String postInfo(User user, HttpSession session, RedirectAttributes attributes){
        User user1 = (User) session.getAttribute("user");
        if(user.getId() != user1.getId()){
                attributes.addFlashAttribute("errormessage", "禁止更改他人的信息");
        }else{
            attributes.addFlashAttribute("message", "修改成功");
        }
        return "redirect:/admin/info";
    }
//    @GetMapping("/login")
//    public String relogin(HttpSession session){
//        return loginPage(session);//加个redirect
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpSession session){
//        session.removeAttribute("user");
//        return "redirect:/admin";
//    }
}
