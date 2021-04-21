package com.ven.vtodo.service;

import com.ven.vtodo.po.Blog;
import com.ven.vtodo.po.User;
import com.ven.vtodo.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.TreeMap;

public interface BlogService {

    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> listBlogByUser(Pageable pageable, BlogQuery blog, User user);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Pageable pageable, Long tagId);

    Page<Blog> listBlog(Pageable pageable, String query);

    List<Blog> listRecommendBlogTop(Integer size);

    TreeMap<String, List<Blog>> archiveBlog();//归档所有数据，不需要参数

    Long countBlog();

    Blog saveBlog(Blog blog);

//    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);
}
