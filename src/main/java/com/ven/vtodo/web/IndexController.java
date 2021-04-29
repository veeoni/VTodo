package com.ven.vtodo.web;

import com.ven.vtodo.po.Note;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.NoteService;
import com.ven.vtodo.service.TagService;
import com.ven.vtodo.service.TypeService;
import com.ven.vtodo.service.UserService;
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

import javax.servlet.http.HttpSession;

/**
 *
 */
@Controller
public class IndexController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);


    @GetMapping("/index")
    public String index(
            @PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user==null){
            user = userService.getUserById(1L);
        }
        //model用于与前端Thymeleaf传递数据，键值对
        model.addAttribute("page", noteService.listNoteByUser(pageable, user));
        model.addAttribute("types", typeService.listTypeTopByUser(6, user));//可定义在配置文件
        model.addAttribute("tags", tagService.listTagTopByUser(10, user));
        model.addAttribute("recommendNotes", noteService.listRecommendNoteTopByUser(8, user));
        logger.info("----------index--------------");
        return "index";
    }
    //通过以下两个方法决定使用commonmark还是editormd显示note
    @GetMapping("/notes/{id}")
    public String note(@PathVariable Long id, Model model){
        Note note = noteService.getAndConvertPublished(id);
        if(note == null){
            return "cannot-access";
        }
        model.addAttribute("note", note);
        return "note";
    }

    @GetMapping("/note/{id}")
    public String note2(@PathVariable Long id, Model model){
        Note note = noteService.getPublishedNote(id);
        if(note == null){
            return "cannot-access";
        }
        model.addAttribute("note", note);
        return "note_editormd";
    }

    @PostMapping("/search")
    public String search(
            @PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String query, Model model){//参数加一个表单中定义的name="query"
        model.addAttribute("page", noteService.listNote(pageable, "%"+query+"%"));
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/footer/newnote")
    public String newnotes(HttpSession session, Model model){
        User user = (User)session.getAttribute("user");
        if(user==null){
            user = userService.getUserById(1L);
        }
        model.addAttribute("newnotes", noteService.listRecommendNoteTopByUser(3, user));
        return "_fragments :: newnoteList";
    }
}
