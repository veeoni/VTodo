package com.ven.vtodo.web;


import com.ven.vtodo.po.Todo;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TodoController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TodoService todoService;
    @Autowired
    private UserService userService;

    @GetMapping
    public String todo(Model model){
        model.addAttribute("types", typeService.listTypeTop(6));//可定义在配置文件
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        System.out.println("----------index--------------");
        return "todo";
    }

    @GetMapping("/todos")
    public String todos(Model model){
        //TODO 分类所有todos
        model.addAttribute("todos", todoService.listTodo());
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        return "todo :: todoList";
    }

    @PostMapping("/todos")
    public String todoSave(Todo todo, HttpSession session){
        User user = (User) session.getAttribute("user");
//        todo.setType(typeService.getType());
        todo.setTags(tagService.listTag(todo.getTagIds()));
        if(user != null){
            todo.setUser(user);
        } else {
            todo.setUser(userService.getUserById(1L));
        }
        System.out.println(todo.toString());
        return "redirect:/todos";
    }

}
