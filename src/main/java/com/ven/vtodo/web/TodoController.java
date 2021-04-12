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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

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
        //前端传值回来，仅会填满可填满的内容，剩下的都为空，如：返回typeid，则，有type对象，但只有id，无name和其他属性。
        //所以这里需要自己查一下并设置以下。吗？
//        to do.setType(typeService.getType(to do.getType().getId()));
        todo.setTags(tagService.listTag(todo.getTagIds()));
        if(user != null){// FIXME 后期这里必须登录才能用，否则不通过
            todo.setUser(user);
        } else {
            todo.setUser(userService.getUserById(1L));
        }
        if(todo.getId()==null){
            System.out.println("-------------------新增");
            todoService.saveTodo(todo);
        }else{
            System.out.println("-------------------修改"+todo.getId());
            todoService.saveTodo(todo);
        }
        System.out.println(todo.toString());
        return "redirect:/todos";
    }

    @GetMapping("/todos/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        todoService.deleteTodo(id);
        System.out.println("-------------------------delete"+id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/";
    }

}
