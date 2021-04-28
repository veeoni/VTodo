package com.ven.vtodo.service;

import com.ven.vtodo.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByNoteId(Long noteId);

    Comment saveComment(Comment comment);
}
