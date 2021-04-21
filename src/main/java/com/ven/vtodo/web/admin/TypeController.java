package com.ven.vtodo.web.admin;

import com.ven.vtodo.po.Type;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("page", typeService.listType(pageable, user));
        return "admin/types";
    }
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable/*保证路径id与此id一致*/ Long id, Model model){
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    //后端消息传到页面
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, HttpSession session, RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        Type type1 = typeService.getTypeByNameAndUser(type.getName(), user);
        if(type1 != null){
            result.rejectValue("name", "nameError", "不能添加重复的分类");
        }
        if(result.hasErrors()){
            return "admin/types-input";
        }
        type.setUser(user);
        Type t = typeService.saveType(type);
        if(t == null){
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/types";
    }

    //后端消息传到页面
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result,/*BindingResult前面一定要是Type，否则就没有效果了*/
                           @PathVariable Long id, HttpSession session, RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        Type type1 = typeService.getTypeByNameAndUser(type.getName(), user);
        if(type1 != null){
            result.rejectValue("name", "nameError", "不能添加重复的分类");
        }
        if(result.hasErrors()){
            return "admin/types-input";
        }
        type.setUser(user);
        Type t = typeService.updateType(id, type);
        if(t == null){
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String  delete(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }

}


