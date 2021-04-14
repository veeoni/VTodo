package com.ven.vtodo.web;


import com.ven.vtodo.po.Todo;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Autowired
    private UserService userService;
    Date date;
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

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
        // TODO 到时候新增一个方法，类似此方法，但是多一个日期参数（可以用String），放URL或者传对象都行，用日期查询
        // 是未来? 仅按日期当日，仅显示未完成:区分是否遗留
        // 数据库里查
        // 1.未finishedDate且taskDate<=指定日期，小于指定日期，isRemain=true，加入todos
        // 2.finishedDate==指定日期，标记为已完成，finished>taskdate的，标记isRemain=true，html改为未按时，加入finishedTodos
        date = new Date();
        List<Todo> todos = todoService.listTodo();
        List<Todo> normalTodos = new ArrayList<>();
        List<Todo> finishedTodos = new ArrayList<>();
        for(Todo todo : todos){
            System.out.println(todo.getTaskDate().toString()+"vs"+sdf.format(date));
            todo.setRemain(todo.getTaskDate().toString().compareTo(sdf.format(date))<0);
            if(todo.getFinishedDate()==null){
                normalTodos.add(todo);
            }else{
                finishedTodos.add(todo);
            }
        }
        //TODO 分类所有todos
        model.addAttribute("todos", normalTodos);
        model.addAttribute("finishedTodos", finishedTodos);
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
