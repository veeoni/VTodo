package com.ven.vtodo.web;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.RoleService;
import com.ven.vtodo.service.UserService;
import com.ven.vtodo.util.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @GetMapping//未使用任何参数时，默认全局的路径,当前为/
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/todo";
        } else {
            return "redirect:/index";
        }
    }

    @GetMapping("/register")
    public String getRegister(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/index";
        }
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String postRegister(User user, @RequestParam String kaptcha, HttpSession session) {
        String verifyCode = (String) session.getAttribute("verifyCode");
        if(!kaptcha.equals(verifyCode)){
            return "验证码错误";
        }
        User user1 = userService.getUserByUsername(user.getUsername());
        if (user1 != null) {
            return "用户名已存在";
        }
        user.setPassword(SHA256Util.getSHA256(user.getUsername() + user.getPassword()));
        user.setRole(roleService.getRoleByName("普通用户"));
        System.out.println(user.getAvatar() + " " + user.getUsername());
        User user2 = userService.saveUser(user);
        if (user2 != null) {
            user2.setPassword(null);
            session.setAttribute("user", user2);
        }
        return "success";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        @RequestParam String kaptcha, HttpSession session, RedirectAttributes attributes) {
        String verifyCode = (String) session.getAttribute("verifyCode");
        if(!kaptcha.equals(verifyCode)){
            attributes.addFlashAttribute("message", "验证码错误");
            return "redirect:/login";
        }
        User user = userService.checkUser(username, password);
        if (user != null) {
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
    public String getLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/todo";
        } else {
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/waiting")//退出登录后的等待界面，3秒返回登陆页。
    public String logout2(HttpSession session) {
        session.removeAttribute("user");
        return "waiting";
    }

    @Autowired
    private DefaultKaptcha captchaProducer;

    @GetMapping("/kaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String verifyCode = captchaProducer.createText();
            httpServletRequest.getSession().setAttribute("verifyCode", verifyCode);
            BufferedImage challenge = captchaProducer.createImage(verifyCode);
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
