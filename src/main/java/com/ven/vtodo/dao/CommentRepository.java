package com.ven.vtodo.dao;

import com.ven.vtodo.po.Comment;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByNoteIdAndParentCommentNull(Long noteId, Sort sort);

    void deleteAllByUser(User user);
}
