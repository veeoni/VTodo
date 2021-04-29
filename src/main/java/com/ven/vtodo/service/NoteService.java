package com.ven.vtodo.service;

import com.ven.vtodo.po.Note;
import com.ven.vtodo.po.User;
import com.ven.vtodo.vo.NoteQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.TreeMap;

public interface NoteService {

    Note getNote(Long id);

    Note getPublishedNote(Long id);

    Note getAndConvert(Long id);

    Note getAndConvertPublished(Long id);
    //管理页用
    Page<Note> listNoteByUser(Pageable pageable, NoteQuery note, User user);

    Page<Note> listNoteByUser(Pageable pageable, User user);

    Page<Note> listNote(Pageable pageable, Long Id, boolean isTag);

    Page<Note> listNote(Pageable pageable, String query);

    List<Note> listRecommendNoteTopByUser(Integer size, User user);

    TreeMap<String, List<Note>> archiveNoteByUser(User user);//归档所有数据

    Long countNotesByUser(User user);

    Long countNote();

    Note saveNote(Note note);

//    Note updateNote(Long id, Note note);

    void deleteNote(Long id);
}
