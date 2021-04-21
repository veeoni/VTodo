package com.ven.vtodo.web;

import com.ven.vtodo.po.Blog;
import com.ven.vtodo.po.Tag;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.BlogService;
import com.ven.vtodo.service.TagService;
import com.ven.vtodo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    @GetMapping("/tags/{id}")
    public String tags(
            @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long id, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user==null){
            user = userService.getUserById(1L);
        }
        List<Tag> tags = tagService.listTagTopByUser(10000, user);
        Page<Blog> page = null;
        if(tags.size()>0){
            if(id==-1){
                id = tags.get(0).getId();
            }
            page = blogService.listBlog(pageable, id);
        }else{
            page = blogService.listBlog(pageable);
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", page);
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
