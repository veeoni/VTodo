package com.ven.vtodo.dao;

import com.ven.vtodo.po.Note;
import com.ven.vtodo.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long>, JpaSpecificationExecutor<Note> {
    @Query("select b from Note b where b.published = true and b.recommend = true")
//只显示发布了的推荐，有些草稿不会查询出来
    List<Note> findTopRecommend(Pageable pageable);

    @Query("select b from Note b where b.user = ?1 and b.published = true and b.recommend = true")
//只显示发布了的推荐，有些草稿不会查询出来
    List<Note> findTopRecommendByUser(User user, Pageable pageable);

//    List<Note> findNotesByContentContainsOrTitleContains();

    Note getNoteByIdAndPublishedTrue(Long id);

    //可以原生sql，select * from t_note where title like '%内容%' and content like '%内容%' nativeQuery = true
    @Query("select b from Note b where (b.title like ?1 or b.content like ?1) and b.published = true ")
//因为两个地方都按query，所以占位符都一样，否则，可用?1 ?2...
    Page<Note> findByQuery(String query, Pageable pageable);//此处并不会为我们加好%%进行匹配，所以要我们自己处理好

    @Transactional
    @Modifying
    @Query("update Note b set b.views = b.views+1 where b.id = ?1")
    void updateViews(Long id);

    @Query(value = "select date_format(b.update_time, '%Y') as year from t_note b where b.published = true GROUP by year ORDER BY year;", nativeQuery = true)
//    @Query("select function('data_format', b.updateTime, '%Y') as year from Note b GROUP BY function('data_format', b.updateTime, '%Y') ORDER BY year DESC")
    List<String> findGroupYears();

    @Query(value = "select date_format(b.update_time, '%Y') as year from t_note b where b.user_id = ?1 and b.published = true GROUP by year ORDER BY year;", nativeQuery = true)
    List<String> findGroupYearsAndUser(Long userId);

    //SELECT * FROM t_note b where date_format(b.update_time, '%Y') = '2016';
    @Query("SELECT b from Note b where function('date_format', b.updateTime, '%Y') = ?1 and b.published = true ")
    List<Note> findByYear(String year);    //SELECT * FROM t_note b where date_format(b.update_time, '%Y') = '2016';

    @Query("SELECT b from Note b where function('date_format', b.updateTime, '%Y') = ?1 and b.user = ?2  and b.published = true order by b.updateTime desc ")
    List<Note> findByYearAndUser(String year, User user);

    @Query("SELECT b from Note b where b.user = ?1 and b.published = true ")
    Page<Note> findAllByUser(User user, Pageable pageable);

    @Query("SELECT count(b) from Note b where b.user = ?1 and b.published = true ")
    long countNotesByUser(User user);

    void deleteAllByUser(User user);
}
