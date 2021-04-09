package com.ven.vtodo.web;

import com.ven.vtodo.po.Todo;
import com.ven.vtodo.service.BlogService;
import com.ven.vtodo.service.TagService;
import com.ven.vtodo.service.TodoService;
import com.ven.vtodo.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
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

    @GetMapping("/")
    public String todo(Model model){
        //model用于与前端Thymeleaf传递数据，键值对
        model.addAttribute("types", typeService.listTypeTop(6));//可定义在配置文件
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
//        List<Todo> todos = todoService.listTodo();
        //TODO 分类所有todos
        model.addAttribute("todos", todoService.listTodo());
        System.out.println("----------index--------------");
        return "todo";
    }

    // TODO: 2021/4/9 ajax刷新todo的那两个segment
    @GetMapping("/todos/{date}")
    private String todoByDate(@PathVariable Date date, Model model){
        
        return "";
    }

    // TODO: 2021/4/9 添加 
}
