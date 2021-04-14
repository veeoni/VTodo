package com.ven.vtodo.service;

import com.ven.vtodo.dao.BlogRepository;
import com.ven.vtodo.handler.NotFoundException;
import com.ven.vtodo.po.Blog;
import com.ven.vtodo.util.MarkdownUtils;
import com.ven.vtodo.vo.BlogQuery;
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
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Transactional
    @Override
    public Blog getBlog(Long id) {
        blogRepository.updateViews(id);
        return blogRepository.getOne(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if(blog == null){
            throw new NotFoundException("该博客不存在！");
        }
        Blog b = new Blog();
//        blog.setViews(blog.getViews()+1);
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        //todo 弹幕说，要注意设置password为空？？
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));//blog.set方法会修改数据库（hibernateSession），这就出错了，所以此处有问题
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override//查谁，条件是啥， 设置具体条件的表达式
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, String query) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
//        return blogRepository.findAll((Specification<Blog>) (root, cq, cb) -> null, pageable);
        return blogRepository.findAll(new Specification<Blog>() {
            @Override//查谁，条件是啥， 设置具体条件的表达式
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(blog.getTitle() != null && !"".equals(blog.getTitle())){
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if(null != blog.getTypeId()){
                    predicates.add(criteriaBuilder.equal(root.<String>get("type").get("id"), blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTopRecommend(pageable);
    }

    @Override
    public TreeMap<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYears();
        System.out.println(years.toString());
        TreeMap<String, List<Blog>> map = new TreeMap<>();
        for(String year : years){
            System.out.println(year);
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

//    @Transactional
//    @Override
//    public Blog updateBlog(Long id, Blog blog) {
//        Blog blog1 = blogRepository.getOne(id);
//        if(blog1 == null){
//            throw new NotFoundException("该博客不存在");
//        }
//        BeanUtils.copyProperties(blog,blog1);
//        return blogRepository.save(blog1);
//    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
