package com.ven.vtodo.service;

import com.ven.vtodo.dao.NoteRepository;
import com.ven.vtodo.handler.NotFoundException;
import com.ven.vtodo.po.Note;
import com.ven.vtodo.po.User;
import com.ven.vtodo.util.MarkdownUtils;
import com.ven.vtodo.vo.NoteQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteRepository noteRepository;
    private static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Transactional
    @Override
    public Note getNote(Long id) {
        noteRepository.updateViews(id);
        return noteRepository.getOne(id);
    }

    @Override
    public Note getPublishedNote(Long id) {
        noteRepository.updateViews(id);
        return noteRepository.getNoteByIdAndPublishedTrue(id);
    }

    @Override
    public Note getAndConvertPublished(Long id) {
        Note note = noteRepository.getNoteByIdAndPublishedTrue(id);
        if (note == null) {
            throw new NotFoundException("该笔记不存在！");
        }
        Note b = new Note();
        BeanUtils.copyProperties(note, b);
        String content = b.getContent();
        //todo 弹幕说，要注意设置password为空？？
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));//note.set方法会修改数据库（hibernateSession），这就出错了，所以此处有问题
        noteRepository.updateViews(id);
        return b;
    }

    @Transactional
    @Override
    public Note getAndConvert(Long id) {
        Note note = noteRepository.getOne(id);
        if (note.getId() == null) {
            throw new NotFoundException("该笔记不存在！");
        }
        Note b = new Note();
//        note.setViews(note.getViews()+1);
        BeanUtils.copyProperties(note, b);
        String content = b.getContent();
        //todo 弹幕说，要注意设置password为空？？
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));//note.set方法会修改数据库（hibernateSession），这就出错了，所以此处有问题
        noteRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Note> listNote(Pageable pageable, Long id, boolean isTag) {
        return noteRepository.findAll(new Specification<Note>() {
            @Override//查谁，条件是啥， 设置具体条件的表达式
            public Predicate toPredicate(Root<Note> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join;
                if (isTag) {
                    join = root.join("tags");
                } else {
                    join = root.join("type");
                }
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.isTrue(root.get("published")));
                predicates.add(criteriaBuilder.equal(join.get("id"), id));
                criteriaQuery.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Note> listNote(Pageable pageable, String query) {
        return noteRepository.findByQuery(query, pageable);
    }

    @Override//管理页查询note
    public Page<Note> listNoteByUser(Pageable pageable, NoteQuery note, User user) {
        return noteRepository.findAll(new Specification<Note>() {
            @Override//查谁，条件是啥， 设置具体条件的表达式
            public Predicate toPredicate(Root<Note> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (user != null && user.getId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Long>get("user").get("id"), user.getId()));
                }
                if (note.getTitle() != null && !"".equals(note.getTitle())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + note.getTitle() + "%"));
                }
                if (null != note.getTypeId()) {
                    predicates.add(criteriaBuilder.equal(root.<Long>get("type").get("id"), note.getTypeId()));
                }
                if (note.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), note.isRecommend()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Note> listNoteByUser(Pageable pageable, User user) {
        return noteRepository.findAllByUser(user, pageable);
    }

    @Override
    public List<Note> listRecommendNoteTopByUser(Integer size, User user) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return noteRepository.findTopRecommendByUser(user, pageable);
    }

    @Override
    public TreeMap<String, List<Note>> archiveNoteByUser(User user) {
        List<String> years = noteRepository.findGroupYearsAndUser(user.getId());
        logger.info(years.toString());
        TreeMap<String, List<Note>> map = new TreeMap<>();
        for (String year : years) {
            logger.info(year);
            map.put(year, noteRepository.findByYearAndUser(year, user));
        }
        return map;
    }

    @Override
    public Long countNotesByUser(User user) {
        return noteRepository.countNotesByUser(user);
    }

    @Override
    public Long countNote() {
        return noteRepository.count();
    }

    @Transactional
    @Override
    public Note saveNote(Note note) {
        if (note.getId() == null) {
            note.setCreateTime(new Date());
            note.setUpdateTime(new Date());
            note.setViews(0);
        } else {
            note.setUpdateTime(new Date());
        }
        if (note.getFirstPicture() == null || note.getFirstPicture().equals("")) {
            note.setFirstPicture("/images/meteor.jpeg");
        }
        return noteRepository.save(note);
    }

//    @Transactional
//    @Override
//    public Note updateNote(Long id, Note note) {
//        Note note1 = noteRepository.getOne(id);
//        if(note1 == null){
//            throw new NotFoundException("该不存笔记在");
//        }
//        BeanUtils.copyProperties(note,note1);
//        return noteRepository.save(note1);
//    }


    @Transactional
    @Override
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }
}
