package com.ven.vtodo.web.admin;

import com.ven.vtodo.po.Blog;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.BlogService;
import com.ven.vtodo.service.TagService;
import com.ven.vtodo.service.TypeService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class NoteController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(
            @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            BlogQuery blog, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("types", typeService.listTypeByUser(user));
        Page page = blogService.listBlogByUser(pageable, blog, user);
        System.out.println(page.toString());
        model.addAttribute("page", page);
        return "admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String search(
            @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            BlogQuery blog, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("page", blogService.listBlogByUser(pageable, blog, user));
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input( HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        setTypeAndTag(user, model);
        model.addAttribute("blog", new Blog());
        return "admin/blogs-input";
    }

    private void setTypeAndTag(User user, Model model){
        model.addAttribute("types", typeService.listTypeByUser(user));
        model.addAttribute("tags", tagService.listTagByUser(user));
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        setTypeAndTag(user, model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        return "admin/blogs-input";
    }


    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b = blogService.saveBlog(blog);
        if(b == null){
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }
}