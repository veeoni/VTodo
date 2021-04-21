package com.ven.vtodo.web;

import com.ven.vtodo.po.Blog;
import com.ven.vtodo.po.Type;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.BlogService;
import com.ven.vtodo.service.TypeService;
import com.ven.vtodo.service.UserService;
import com.ven.vtodo.vo.BlogQuery;
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
public class TypeShowController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private UserService userService;

    @GetMapping("/types/{id}")
    public String types(
            @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long id, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user == null){
            user = userService.getUserById(1L);
        }
        List<Type> types = typeService.listTypeTopByUser(10000, user);
        BlogQuery blog = new BlogQuery();
        Page<Blog> page = null;
        if(types.size()>0){
            if(id==-1){
                id = types.get(0).getId();
            }
            blog.setTypeId(id);
            page = blogService.listBlogByUser(pageable, blog, user);
        }else{
            page = blogService.listBlogByUser(pageable, user);
        }
        model.addAttribute("types", types);
        model.addAttribute("page", page);
        model.addAttribute("activeTypeId", id);
        return "types";
    }
}
