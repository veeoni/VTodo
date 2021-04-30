package com.ven.vtodo.web.admin;

import com.ven.vtodo.po.User;
import com.ven.vtodo.service.UserService;
import com.ven.vtodo.util.SHA256Util;
import com.ven.vtodo.util.UpdateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class InfoController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")//未使用任何参数时，默认全局的路径,当前为/admin
    public String infoPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
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
    public String postInfo(User user, HttpSession session, RedirectAttributes attributes) {
        User user1 = (User) session.getAttribute("user");
        if (!user.getId().equals(user1.getId())) {
            attributes.addFlashAttribute("errormessage", "禁止更改他人的信息");
        } else if (!user.getUsername().equals(user1.getUsername())) {
            attributes.addFlashAttribute("errormessage", "禁止更改用户名");
        } else {
            User oldUser = userService.getUserById(user.getId());
            UpdateUtil.copyNullProperties(user, oldUser);
            oldUser.setUpdateTime(new Date());
            User user2 = userService.updateUserInfo(oldUser);
            if (user2 != null) {
                attributes.addFlashAttribute("message", "修改成功");
            } else {
                attributes.addFlashAttribute("errormessage", "修改信息失败");
            }
        }
        return "redirect:/admin/info";
    }

    @GetMapping("/passchange")//未使用任何参数时，默认全局的路径,当前为/admin
    public String passchangePage(HttpSession session, Model model, RedirectAttributes attributes) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            User user2 = userService.getUserById(user.getId());
            user2.setPassword(null);
            session.setAttribute("user", user2);
            model.addAttribute("user", user2);
            return "admin/passchange";
        } else if ((boolean) session.getAttribute("relogin")) {
            return "redirect:/admin/passchange";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/passchange")
    public String postPasschange(User user, @RequestParam String postPassword, HttpSession session, RedirectAttributes attributes) {
        User user1 = (User) session.getAttribute("user");
        if (!user.getId().equals(user1.getId())) {
            attributes.addFlashAttribute("errormessage", "禁止更改他人的信息");
        } else if (!user.getUsername().equals(user1.getUsername())) {
            attributes.addFlashAttribute("errormessage", "禁止更改用户名");
        } else {
            User oldUser = userService.checkUser(user.getUsername(), user.getPassword());
            if (oldUser == null || !oldUser.getId().equals(user.getId())) {
                attributes.addFlashAttribute("errormessage", "原密码错误");
            } else {
                oldUser.setPassword(SHA256Util.getSHA256(oldUser.getUsername() + postPassword));
                oldUser.setUpdateTime(new Date());
                User user2 = userService.updateUserInfo(oldUser);
                if (user2 != null) {
                    attributes.addFlashAttribute("message", "修改成功");
//                    session.removeAttribute("user");//不需要在这里登出，去页面倒计时登出
                    session.setAttribute("relogin", true);
                } else {
                    attributes.addFlashAttribute("errormessage", "修改信息失败");
                }
            }
        }
        return "redirect:/admin/passchange";
    }
}
