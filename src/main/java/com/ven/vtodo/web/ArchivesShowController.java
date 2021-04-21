package com.ven.vtodo.web;

import com.ven.vtodo.po.User;
import com.ven.vtodo.service.BlogService;
import com.ven.vtodo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ArchivesShowController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    @GetMapping("/archives")
    public String archives(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user == null){
            user = userService.getUserById(1L);
        }
        model.addAttribute("archiveMap", blogService.archiveBlogByUser(user).descendingMap());
        model.addAttribute("blogCount", blogService.countBlogsByUser(user));
        return "archives";
    }
}
