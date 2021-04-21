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

    Page<Blog> listBlogByUser(Pageable pageable, User user);

    Page<Blog> listBlog(Pageable pageable, Long tagId);

    Page<Blog> listBlog(Pageable pageable, String query);

    List<Blog> listRecommendBlogTopByUser(Integer size, User user);

    TreeMap<String, List<Blog>> archiveBlogByUser(User user);//归档所有数据

    Long countBlogsByUser(User user);

    Long countBlog();

    Blog saveBlog(Blog blog);

//    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);
}
