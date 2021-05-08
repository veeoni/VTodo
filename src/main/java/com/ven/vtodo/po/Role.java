package com.ven.vtodo.po;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "t_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users = new ArrayList<>();
    @ManyToMany(mappedBy = "roles")
    private List<Permission> permissions = new ArrayList<>();

    public Role() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users[0]Id=" + (users != null && users.size() > 0 ? users.get(0).getId() : "null") +
                ", permissions[0]Id=" + (permissions != null && permissions.size() > 0 ? permissions.get(0).getId() : "null") +
                '}';
    }
}
