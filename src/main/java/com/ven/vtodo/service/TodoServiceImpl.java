package com.ven.vtodo.service;

import com.ven.vtodo.dao.TodoRepository;
import com.ven.vtodo.po.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                System.out.println("无此flag");
            }
            todo.setRemainTimes(todo.getTotalTimes());
        } else if (todo.getId()==-1L) {
            //用于保存暂时完成的记录
            todo.setId(null);
        }else{
            todo.setUpdateTime(new Date());
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
                todo.setRemainTimes(todo.getTotalTimes());
            }else if(todo.getFlag().equals("复习")){//是复习条目
                //修改复习条目时，不更改原有信息
//                todo.setInterval(1.0);
//                todo.setEasinessFactor(2.0);
//                todo.setTotalTimes(65535);
            } else {
                System.out.println("无此flag");
            }
        }
        return todoRepository.save(todo);
    }

    @Override
    public Todo getTodo(Long id) {
        return todoRepository.getOne(id);
    }

    @Override
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
