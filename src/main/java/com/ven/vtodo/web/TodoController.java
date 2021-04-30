package com.ven.vtodo.web;


import com.ven.vtodo.po.Tag;
import com.ven.vtodo.po.Todo;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class TodoController {
    @Autowired
    private CountdownService countdownService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TodoService todoService;
    @Autowired
    private TargetService targetService;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final Calendar calendar = Calendar.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    @GetMapping("/todo")
    public String todo(HttpSession session, RedirectAttributes attributes, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            attributes.addFlashAttribute("message", "您需要先登录，才能使用日程规划功能");
            return "redirect:/login";
        }
        model.addAttribute("targets", targetService.listTargetByUser(user));
        model.addAttribute("countdowns", countdownService.listCountdownByUser(user));
        logger.info("----------index--------------");
        return "todo";
    }

    @GetMapping("/alltodos")//用来查整张表的待办
    public String todos(HttpSession session, Model model) {
        // TODO 到时候新增一个方法，类似此方法，但是多一个日期参数（可以用String），放URL或者传对象都行，用日期查询
        // 是未来? 仅按日期当日，仅显示未完成:区分是否遗留
        // 数据库里查
        // 1.未finishedDate且taskDate<=指定日期，小于指定日期，isRemain=true，加入todos
        // 2.finishedDate==指定日期，标记为已完成，finished>taskdate的，标记isRemain=true，html改为未按时，加入finishedTodos
        Date date = new Date();
        List<Todo> todos = todoService.listTodo();
        List<Todo> normalTodos = new ArrayList<>();
        List<Todo> finishedTodos = new ArrayList<>();
        for (Todo todo : todos) {
            logger.info(todo.getTaskDate().toString() + "vs" + sdf.format(date));
            todo.setRemain(todo.getTaskDate().toString().compareTo(sdf.format(date)) < 0);
            if (todo.getFinishedDate() == null) {
                normalTodos.add(todo);
            } else {
                finishedTodos.add(todo);
            }
        }
        User user = (User) session.getAttribute("user");
        model.addAttribute("todos", normalTodos);
        model.addAttribute("finishedTodos", finishedTodos);
        model.addAttribute("types", typeService.listTypeByUser(user));
        model.addAttribute("tags", tagService.listTagByUser(user));
        return "todo :: todoList";
    }

    @GetMapping(value = {"/todos/{strDate}", "/todos"})
    public String todosByDate(@Nullable @PathVariable String strDate, HttpSession session, Model model) throws ParseException {

        // 是未来? 仅按日期当日，仅显示未完成:区分是否遗留
        // 数据库里查
        // 1.未finishedDate且taskDate<=指定日期，小于指定日期，isRemain=true，加入todos
        // 2.finishedDate==指定日期，标记为已完成，finished>taskdate的，标记isRemain=true，html改为未按时，加入finishedTodos
        User user = (User) session.getAttribute("user");
        Date date;
        boolean isToday = false;
        if (strDate == null || strDate.equals("")) {
            strDate = sdf.format(new Date());
            isToday = true;
        } else if (strDate.equals(sdf.format(new Date()))) {
            isToday = true;
        }
        date = sdf.parse(strDate);
        List<Todo> unfinishedTodos = isToday ? todoService.listUnfinishedTodosByDateAndUser(date, user) : todoService.listUnfinishedTodosByOtherDateAndUser(date, user);
        List<Todo> finishedTodos = todoService.listFinishedTodosByDateAndUser(date, user);
        for (Todo todo : unfinishedTodos) {
            logger.info(todo.getTaskDate().toString() + "vs" + sdf.format(date));
            todo.setRemain(todo.getTaskDate().toString().compareTo(sdf.format(date)) < 0);
        }
        for (Todo todo : finishedTodos) {
            logger.info(todo.getTaskDate().toString() + "vs" + sdf.format(date));
            todo.setRemain(todo.getTaskDate().toString().compareTo(sdf.format(date)) < 0);
        }
        model.addAttribute("todos", unfinishedTodos);
        model.addAttribute("finishedTodos", finishedTodos);
        model.addAttribute("types", typeService.listTypeByUser(user));
        model.addAttribute("tags", tagService.listTagByUser(user));
        model.addAttribute("queryDate", strDate);
        return "todo :: todoList";
    }

    @GetMapping("/todos/{id}/finish")//完成待办
    public String finished(@PathVariable Long id, RedirectAttributes attributes) {
        Todo todo = todoService.getTodo(id);
        logger.info("待办,id=" + id);

        todo.setRemainTimes(todo.getRemainTimes() - 1);
        if (todo.getRemainTimes() == 0) {
            //完成待办
            todo.setFinishedDate(new Date());
        } else {
            // 为多次待办添加已完成记录的方法，两种解决方案：
            // 1.新增一个完成记录（方法一增加的记录太多了，但是，为了方便未来的查询，还是有必要的）
            // 2.给待办设置finishedDate，同时remainTimes--，
            //   在分类处，以remainTimes+finishedDate为依据，判断是否完成，
            //   这就要保证remainTimes、TotalTimes的严格设置
            //   这个方法的问题在于，日后往前查的时候，很多已完成待办会消失
            // 综上所述，为了方便日后的查询，这里选择方法一
            // 又想到一种方案，给finishedDate做成list，这应该会是最优解（保证唯一性，允许进行编辑），但是先不考虑，等整个系统实现，再进行重构
            Todo todoCpy = copyTodo(todo);
            todoCpy.setFinishedDate(new Date());
            todoCpy.setId(-1L);//-1说明，该todo是暂时完成的多次待办，此处用来存储完成的记录
            todoService.saveFinishedTodo(todoCpy);
            // 这里有点考究，如果推迟完成了，那是按原规定出现，还是以完成日期为基础推进？暂定后者，
            // TODO 自主选择原规定还是完成日记推进
            calendar.setTime(new Date()); //需要将date数据转移到Calender对象中操作
            calendar.add(Calendar.DATE, todo.getInterval().intValue());//把日期往后增加n天.正数往后推,负数往前移动
            todo.setTaskDate(calendar.getTime());//这个时间就是日期往后推n天的结果
        }
        todoService.saveFinishedTodo(todo);//此处可以考虑重新写一个save方法，都调用todoRepository的saveTodo就行。
        logger.info("-------------------------finished" + id);
        attributes.addFlashAttribute("message", "已完成");
        return "redirect:/";
    }

    private Todo copyTodo(Todo todo) {
        Todo todoCpy = new Todo();
        BeanUtils.copyProperties(todo, todoCpy);
        List<Tag> tags = tagService.listTagByTodo(todo);
        List<Tag> newTags = new ArrayList<>(tags.size());
        tags.forEach(tag -> newTags.add(tag));
        todoCpy.setTags(newTags);
        logger.info(tags.toString() + " " + newTags.toString());
        return todoCpy;
    }

    @GetMapping("/todos/{id}/finish/{rate}")//完成复习
    public String finished(@PathVariable Long id, @PathVariable int rate, RedirectAttributes attributes) {
        Todo todo = todoService.getTodo(id);
        logger.info("复习，id=" + id + ",rate=" + rate);
        todo.setRemainTimes(todo.getRemainTimes() - 1);
        //保存一条已完成的记录
        Todo todoCpy = copyTodo(todo);

        todoCpy.setFinishedDate(new Date());
        todoCpy.setId(-1L);//-1说明，该todo是暂时完成的多次待办，此处用来存储完成的记录
        todoService.saveFinishedTodo(todoCpy);

        Double interval = todo.getInterval();
        Double EF = todo.getEasinessFactor();
        logger.info("原EF=" + EF + ",原Interval=" + interval);
        //更新EF,EF==4时，EF不改变，这里加速计算
        if (rate != 4) {
            EF = EF - 0.8 + (0.28 - 0.02 * rate) * rate;
            if (EF > 2.5) EF = 2.5;
            else if (EF < 1.1) EF = 1.1;
            todo.setEasinessFactor(EF);
        }
        if (interval < 6) {
            interval = interval + rate - 1;
            if (rate == 4) {
                interval--;
            }
            todo.setInterval(interval);
        } else {
            todo.setInterval(interval * EF);
        }
        logger.info("现EF=" + EF + ",现Interval=" + interval);
        calendar.setTime(new Date()); //需要将date数据转移到Calender对象中操作
        calendar.add(Calendar.DATE, todo.getInterval().intValue());//把日期往后增加n天.正数往后推,负数往前移动
        todo.setTaskDate(calendar.getTime());//这个时间就是日期往后推一天的结果
        todoService.saveFinishedTodo(todo);
        logger.info("-------------------------finished" + id);
        attributes.addFlashAttribute("message", "已完成");
        return "redirect:/";
    }


    @PostMapping("/todos/{strDate}")
    public String todoSave(Todo todo, @PathVariable String strDate, HttpSession session) {
        User user = (User) session.getAttribute("user");
        //前端传值回来，仅会填满可填满的内容，剩下的都为空，如：返回typeid，则，有type对象，但只有id，无name和其他属性。
        //所以这里需要自己查一下并设置以下。吗？
//        to do.setType(typeService.getType(to do.getType().getId()));
        todo.setTags(tagService.listTag(todo.getTagIds()));
        if (user != null) {// FIXME 后期这里必须登录才能用，否则不通过
            todo.setUser(user);
        } else {
//            todo.setUser(userService.getUserById(1L));
            return "redirect:/login";
        }
        if (todo.getId() == null) {
            logger.info("-------------------新增");
            todoService.saveTodo(todo);
        } else {
            logger.info("-------------------修改" + todo.getId());
            todoService.saveTodo(todo);
        }
        logger.info(todo.toString());
        return "redirect:/todos/" + strDate;
    }

    @GetMapping("/todos/{id}/delete/{taskDate}")
    public String delete(@PathVariable Long id, @PathVariable String taskDate, RedirectAttributes attributes) {
        todoService.deleteTodo(id);
        logger.info("-------------------------delete" + id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/todos/" + taskDate;
    }

}
