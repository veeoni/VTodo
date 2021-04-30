package com.ven.vtodo.web;

import com.ven.vtodo.po.Note;
import com.ven.vtodo.po.Type;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.NoteService;
import com.ven.vtodo.service.TypeService;
import com.ven.vtodo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Controller
public class TypeShowController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private UserService userService;

    @GetMapping("/types/{id}")
    public String types(
            @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = userService.getUserById(1L);
        }
        List<Type> types = typeService.listTypeTopByUser(10000, user);
        Page<Note> page = null;
        if (types.size() > 0) {
            if (id == -1) {
                id = types.get(0).getId();
            }
            page = noteService.listNote(pageable, id, false);
        } else {
            page = new Page<Note>() {
                @Override
                public int getTotalPages() {
                    return 0;
                }

                @Override
                public long getTotalElements() {
                    return 0;
                }

                @Override
                public <U> Page<U> map(Function<? super Note, ? extends U> function) {
                    return null;
                }

                @Override
                public int getNumber() {
                    return 0;
                }

                @Override
                public int getSize() {
                    return 0;
                }

                @Override
                public int getNumberOfElements() {
                    return 0;
                }

                @Override
                public List<Note> getContent() {
                    return null;
                }

                @Override
                public boolean hasContent() {
                    return false;
                }

                @Override
                public Sort getSort() {
                    return null;
                }

                @Override
                public boolean isFirst() {
                    return false;
                }

                @Override
                public boolean isLast() {
                    return false;
                }

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public boolean hasPrevious() {
                    return false;
                }

                @Override
                public Pageable nextPageable() {
                    return null;
                }

                @Override
                public Pageable previousPageable() {
                    return null;
                }

                @Override
                public Iterator<Note> iterator() {
                    return null;
                }
            };
        }
        model.addAttribute("types", types);
        model.addAttribute("page", page);
        model.addAttribute("activeTypeId", id);
        return "types";
    }
}
