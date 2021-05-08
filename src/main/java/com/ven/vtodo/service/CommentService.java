package com.ven.vtodo.service;

import com.ven.vtodo.po.Comment;
import com.ven.vtodo.po.User;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByNoteId(Long noteId);

    Comment saveComment(Comment comment);

    void deleteCommentByUser(User user);
}
