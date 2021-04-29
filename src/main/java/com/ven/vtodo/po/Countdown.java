package com.ven.vtodo.po;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "t_countdown")
public class Countdown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "倒计时不能为空")
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//th传值时，需要的是String，此处是Date，类型不匹配，所以要转换
    private Date countTime;
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//th传值时，需要的是String，此处是Date，类型不匹配，所以要转换
    private Date createTime;
    private boolean isShow;
    @ManyToOne
    private User user;

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean show) {
        isShow = show;
    }

    public void setIsShow(boolean show) {
        isShow = show;
    }


    public Date getCountTime() {
        return countTime;
    }

    public void setCountTime(Date countTime) {
        this.countTime = countTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Countdown() {
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
        return "Countdown{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", countTime=" + countTime +
                ", createTime=" + createTime +
                ", isShow=" + isShow +
                ", userId=" + (user!=null?user.getId():"null") +
                '}';
    }
}
