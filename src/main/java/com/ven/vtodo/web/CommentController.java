package com.ven.vtodo.web;

import com.ven.vtodo.po.Comment;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.NoteService;
import com.ven.vtodo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private NoteService noteService;
    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{noteId}")
    public String comments(@PathVariable Long noteId, Model model) {
        model.addAttribute("comments", commentService.listCommentByNoteId(noteId));
        return "note_editormd :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        //传noteId，就建立了note和comment的关系
        Long noteId = comment.getNote().getId();
        comment.setNote(noteService.getNote(noteId));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setIsAuthor(true);
            comment.setUser(user);
            comment.setAvatar(user.getAvatar());
            comment.setEmail(user.getEmail());
            comment.setNickname(user.getNickname());
        } else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + noteId;
    }
}
