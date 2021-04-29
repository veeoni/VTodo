package com.ven.vtodo.web.admin;

import com.ven.vtodo.po.Target;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.TargetService;
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
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class TargetController {

    @Autowired
    private TargetService targetService;

    @GetMapping("/targets")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("page", targetService.listTarget(pageable, user));
        return "admin/targets";
    }
    @GetMapping("/targets/input")
    public String input(Model model){
        model.addAttribute("target", new Target());
        return "admin/targets-input";
    }

    @GetMapping("/targets/{id}/input")
    public String editInput(@PathVariable/*保证路径id与此id一致*/ Long id, Model model){
        model.addAttribute("target", targetService.getTarget(id));
        return "admin/targets-input";
    }

    //后端消息传到页面
    @PostMapping("/targets")
    public String post(@Valid Target target, BindingResult result, HttpSession session, RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        Target target1 = targetService.getTargetByNameAndUser(target.getName(), user);
        target.setCreateTime(new Date());
        target.setUser(user);
        if(target1 != null){
            result.rejectValue("name", "nameError", "已有同名目标");
            return "admin/targets-input";
        }
        Target t = targetService.saveTarget(target);
        if(t == null){
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/targets";
    }

    //后端消息传到页面
    @PostMapping("/targets/{id}")
    public String editPost(@Valid Target target, BindingResult result,/*BindingResult前面一定要是Target，否则就没有效果了*/
                           @PathVariable Long id, HttpSession session, RedirectAttributes attributes){
        User user = (User) session.getAttribute("user");
        target.setUser(user);
        Target t = targetService.updateTarget(id, target);
        if(t == null){
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/targets";
    }

    @GetMapping("/targets/{id}/delete")
    public String  delete(@PathVariable Long id, RedirectAttributes attributes){
        targetService.deleteTarget(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/targets";
    }

}


