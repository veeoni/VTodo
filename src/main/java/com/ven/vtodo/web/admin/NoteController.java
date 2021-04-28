package com.ven.vtodo.web.admin;

import com.ven.vtodo.po.Note;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.NoteService;
import com.ven.vtodo.service.TagService;
import com.ven.vtodo.service.TypeService;
import com.ven.vtodo.vo.NoteQuery;
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
    private NoteService noteService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/notes")
    public String notes(
            @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            NoteQuery note, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("types", typeService.listTypeByUser(user));
        Page page = noteService.listNoteByUser(pageable, note, user);
        System.out.println(page.toString());
        model.addAttribute("page", page);
        return "admin/notes";
    }

    @PostMapping("/notes/search")
    public String search(
            @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            NoteQuery note, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("page", noteService.listNoteByUser(pageable, note, user));
        return "admin/notes :: noteList";
    }

    @GetMapping("/notes/input")
    public String input( HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        setTypeAndTag(user, model);
        model.addAttribute("note", new Note());
        return "admin/notes-input";
    }

    private void setTypeAndTag(User user, Model model){
        model.addAttribute("types", typeService.listTypeByUser(user));
        model.addAttribute("tags", tagService.listTagByUser(user));
    }

    @GetMapping("/notes/{id}/input")
    public String editInput(@PathVariable Long id, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        setTypeAndTag(user, model);
        Note note = noteService.getNote(id);
        note.init();
        model.addAttribute("note", note);
        return "admin/notes-input";
    }


    @PostMapping("/notes")
    public String post(Note note, RedirectAttributes attributes, HttpSession session){
        note.setUser((User) session.getAttribute("user"));
        note.setType(typeService.getType(note.getType().getId()));
        note.setTags(tagService.listTag(note.getTagIds()));
        Note b = noteService.saveNote(note);
        if(b == null){
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/notes";
    }

    @GetMapping("/notes/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        noteService.deleteNote(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/notes";
    }
}