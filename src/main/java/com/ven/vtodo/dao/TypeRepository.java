package com.ven.vtodo.dao;

import com.ven.vtodo.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long>  {
    Type findByName(String name);
    //JpaRepository未提供的方法，可以自己按照restful风格创建。
    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
