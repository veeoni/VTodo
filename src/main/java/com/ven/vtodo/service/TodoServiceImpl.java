package com.ven.vtodo.service;

import com.ven.vtodo.dao.TagRepository;
import com.ven.vtodo.dao.TodoRepository;
import com.ven.vtodo.po.Tag;
import com.ven.vtodo.po.Todo;
import com.ven.vtodo.util.TodoCopy;
import com.ven.vtodo.web.TodoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<Todo> listTodo() {
        return todoRepository.findAll();
    }

    private static Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    @Transactional
    @Override
    public Todo saveTodo(Todo todo) {
        //新增 id==null
        if(todo.getId()==null){
            todo.setCreateTime(new Date());
            todo.setUpdateTime(todo.getCreateTime());
            if(todo.getFlag().equals("待办")){
                //此处必须用getRepeat，否则，万一提前修改了totalTimes，然后选了flag=“复习”,有可能出错
                // （可以clearContent）但是还是要双重保险
                if(todo.getRepeat()){
                    //重复待办
                    if(todo.getTotalTimes()==0){
                        todo.setFinishedDate(new Date());
                    } else if(todo.getTotalTimes() == -1) {
                        todo.setTotalTimes(65535);
                    }
                } else {
                    //单次待办
                    todo.setTotalTimes(1);
                }
            }else if(todo.getFlag().equals("复习")){//是复习条目
                todo.setInterval(1.0);
                todo.setEasinessFactor(2.0);
                todo.setTotalTimes(65535);
            } else {
                logger.info("无此flag");
            }
            todo.setRemainTimes(todo.getTotalTimes());
        } else if (todo.getId()==-1L) {
            //用于保存暂时完成的记录
            todo.setId(null);
        }else{//编辑功能
            todo.setUpdateTime(new Date());
//            if(todo.getFinishedDate()==null){
                if(todo.getFlag().equals("待办")){
                    //此处必须用getRepeat，否则，万一提前修改了totalTimes，然后选了flag=“复习”,有可能出错
                    // （可以clearContent）但是还是要双重保险
                    if(todo.getRepeat()){//既然在修改时选择了重复，那校验就是打开的，所以，如果不是0和-1，就按照编辑的来
                        //重复待办
                        if(todo.getTotalTimes()==0){
                            todo.setFinishedDate(new Date());
                        } else if(todo.getTotalTimes() == -1) {
                            todo.setTotalTimes(65535);
                        }
                    } else {
                        //单次待办
                        todo.setTotalTimes(1);
                        todo.setInterval(1.0);
                    }
                    todo.setRemainTimes(todo.getTotalTimes());
                }else if(todo.getFlag().equals("复习")){//是复习条目
                    //修改复习条目时，不更改原有信息
                    Todo todo2 = this.getTodo(todo.getId());
                    todo.setTotalTimes(todo2.getTotalTimes());
                    todo.setRemainTimes(todo2.getRemainTimes());
                    todo.setEasinessFactor(todo2.getEasinessFactor());
                    todo.setInterval(todo2.getInterval());
                } else {
                    logger.info("无此flag");
                }
//            }
        }
        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> listUnfinishedTodosByDate(Date date) {
        return todoRepository.findAllByTaskDateLessThanEqualAndFinishedDateNull(date);
    }

    @Override
    public List<Todo> listFinishedTodosByDate(Date date) {
        return todoRepository.findAllByFinishedDateEquals(date);
    }

    @Override
    public List<Todo> listUnfinishedTodosByOtherDate(Date date) {
        return todoRepository.findAllByTaskDateEqualsAndFinishedDateNull(date);
    }

    @Override
    public Todo saveFinishedTodo(Todo todo) {
        todo.setUpdateTime(new Date());
        return todoRepository.save(todo);
    }

    @Override
    public Todo getTodo(Long id) {
        return todoRepository.getOne(id);
    }

    @Transactional
    @Override
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
