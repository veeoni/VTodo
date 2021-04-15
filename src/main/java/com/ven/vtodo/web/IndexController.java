package com.ven.vtodo.web;

import com.ven.vtodo.service.BlogService;
import com.ven.vtodo.service.TagService;
import com.ven.vtodo.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);


    @GetMapping("/index")
    public String index(
            @PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            Model model){
        //model用于与前端Thymeleaf传递数据，键值对
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));//可定义在配置文件
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        logger.info("----------index--------------");
        return "index";
    }
    //通过以下两个方法决定使用commonmark还是editormd显示blog
    @GetMapping("/blogs/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/blog/{id}")
    public String blog2(@PathVariable Long id, Model model){
        model.addAttribute("blog", blogService.getBlog(id));
        return "blog_editormd";
    }

    @PostMapping("/search")
    public String search(
            @PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String query, Model model){//参数加一个表单中定义的name="query"
        model.addAttribute("page", blogService.listBlog(pageable, "%"+query+"%"));
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}
