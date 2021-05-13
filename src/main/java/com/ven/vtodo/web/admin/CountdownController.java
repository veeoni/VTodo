package com.ven.vtodo.web.admin;

import com.ven.vtodo.po.Countdown;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.CountdownService;
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
public class CountdownController {

    @Autowired
    private CountdownService countdownService;

    @GetMapping("/countdowns")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("page", countdownService.listCountdown(pageable, user));
        return "admin/countdowns";
    }

    @GetMapping("/countdowns/input")
    public String input(Model model) {
        model.addAttribute("countdown", new Countdown());
        return "admin/countdowns-input";
    }

    @GetMapping("/countdowns/{id}/input")
    public String editInput(@PathVariable/*保证路径id与此id一致*/ Long id, Model model) {
        model.addAttribute("countdown", countdownService.getCountdown(id));
        return "admin/countdowns-input";
    }

    //添加
    @PostMapping("/countdowns")
    public String post(@Valid Countdown countdown, BindingResult result, HttpSession session, RedirectAttributes attributes) {
        User user = (User) session.getAttribute("user");
        Countdown countdown1 = countdownService.getCountdownByNameAndUser(countdown.getName(), user);
        countdown.setCreateTime(new Date());
        countdown.setUser(user);
        if (countdown1 != null) {
            result.rejectValue("name", "nameError", "已有同名倒计时");
        }
        if (result.hasErrors()) {
            return "admin/countdowns-input";
        }
        Countdown t = countdownService.saveCountdown(countdown);
        if (t == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/countdowns";
    }

    //更新
    @PostMapping("/countdowns/{id}")
    public String editPost(@Valid Countdown countdown, BindingResult result,/*BindingResult前面一定要是Countdown，否则就没有效果了*/
                           @PathVariable Long id, HttpSession session, RedirectAttributes attributes) {
        User user = (User) session.getAttribute("user");
        Countdown countdown1 = countdownService.getCountdownByNameAndUser(countdown.getName(), user);
        if (!countdown1.getId().equals(id)) {
            result.rejectValue("name", "nameError", "已有同名倒计时");
        }
        if (result.hasErrors()) {
            return "admin/countdowns-input";
        }
        countdown.setUser(user);
        Countdown t = countdownService.updateCountdown(id, countdown);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/countdowns";
    }

    @GetMapping("/countdowns/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        countdownService.deleteCountdown(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/countdowns";
    }

}


