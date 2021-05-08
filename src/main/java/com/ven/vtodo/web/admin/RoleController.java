package com.ven.vtodo.web.admin;

import com.ven.vtodo.po.Permission;
import com.ven.vtodo.po.Role;
import com.ven.vtodo.po.User;
import com.ven.vtodo.service.PermissionService;
import com.ven.vtodo.service.RoleService;
import com.ven.vtodo.service.UserService;
import com.ven.vtodo.util.UpdateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    @GetMapping("/roles")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", roleService.listRole(pageable));
        return "admin/roles";
    }

    @GetMapping("/users")
    public String users(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                        HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("page", userService.listUser(pageable));
            return "admin/users";
        } else {
            return "redirect:/login";
        }
    }

    //进入用户修改页
    @GetMapping("/setroles/{id}/input")
    public String setRoles(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes attributes) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            User u = userService.getUserById(id);
            if(u==null){
                attributes.addFlashAttribute("errormessage", "无此用户");
                return "redirect:/admin/users";
            }
            model.addAttribute("user", u);
            model.addAttribute("roles", roleService.listRole());
            return "admin/setroleforuser";
        } else {
            return "redirect:/login";
        }
    }
    //修改其他用户的信息
    @PostMapping("/setroles")
    public String postSetRoles(User user, RedirectAttributes attributes) {
        User oldUser = userService.getUserById(user.getId());
        UpdateUtil.copyNullProperties(user, oldUser);
        oldUser.setUpdateTime(new Date());
        User user2 = userService.updateUserInfo(oldUser);
        if (user2 != null) {
            attributes.addFlashAttribute("message", "修改成功");
        } else {
            attributes.addFlashAttribute("errormessage", "修改信息失败");
        }
        return "redirect:/admin/setroles/"+user.getId()+"/input";
    }

    @GetMapping("/roles/input")
    public String input(Model model) {
        model.addAttribute("permissions", permissionService.getAll());
        model.addAttribute("role", new Role());
        return "admin/roles-input";
    }

    @GetMapping("/roles/{id}/input")
    public String editInput(@PathVariable/*保证路径id与此id一致*/ Long id, Model model, RedirectAttributes attributes) {
        if(id == 1 || id == 2){
            attributes.addFlashAttribute("errormessage", "禁止修改系统默认角色");
            return "redirect:/admin/roles";
        }
        model.addAttribute("permissions", permissionService.getAll());
        Role role = roleService.getRoleById(id);
        model.addAttribute("role", role);
        model.addAttribute("permissionIds", role.getPermissionIds());
        return "admin/roles-input";
    }

    //后端消息传到页面
    @PostMapping("/roles")
    public String post(@Valid Role role, BindingResult result, HttpSession session, RedirectAttributes attributes,
                       Model model) {
        Role role1 = roleService.getRoleByName(role.getName());
        if (role1 != null) {
            result.rejectValue("name", "nameError", "不能添加重复的角色");
        }
        if (result.hasErrors()) {
            model.addAttribute("permissions", permissionService.getAll());
            return "admin/roles-input";
        }
        String ids = role.getPermissionIds();
        List<Permission> permissions = permissionService.listPermissionByIds(ids);
        role.setPermissions(permissions);
        Role r = roleService.saveRole(role);
        if (r == null) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/roles";
    }

    //后端消息传到页面
    @PostMapping("/roles/{id}")
    public String editPost(@Valid Role role, BindingResult result,/*BindingResult前面一定要是Role，否则就没有效果了*/
                           @PathVariable Long id, HttpSession session, RedirectAttributes attributes, Model model) {
        Role role1 = roleService.getRoleByName(role.getName());
        if (role1 != null) {
            result.rejectValue("name", "nameError", "不能添加重复的角色");
        }
        if (result.hasErrors()) {
            model.addAttribute("permissions", permissionService.getAll());
            return "admin/roles-input";
        }
        String ids = role.getPermissionIds();
        List<Permission> permissions = permissionService.listPermissionByIds(ids);
        role.setPermissions(permissions);
        Role t = roleService.updateRole(id, role);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/roles";
    }


    @GetMapping("/roles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        if(id == 1 || id == 2){
            attributes.addFlashAttribute("errormessage", "禁止删除系统默认角色");
            return "redirect:/admin/roles";
        }
        List<User> users = userService.getUserByRoleId(id);
        if(users.size()>0){
            setDefaultPermission(users);
        }
        roleService.deleteRole(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/roles";
    }
    private boolean setDefaultPermission(List<User> users){
        for(User user: users){
            user.setRole(roleService.getRoleByName("普通用户"));
        }
        return true;
    }

}


