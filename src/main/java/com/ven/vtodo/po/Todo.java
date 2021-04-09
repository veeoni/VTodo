package com.ven.vtodo.po;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @Temporal(TemporalType.DATE)
    private Date finishedTime;
    @Temporal(TemporalType.DATE)
    private Date taskTime;
    @Column(name = "m_interval")
    private Integer interval;
    private Integer totalTimes;
    private Integer remainTimes;

    @ManyToOne
    private User user;
    @ManyToOne
    private Type type;
    @ManyToMany
    private List<Tag> tags;

    public Todo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Date getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Date taskTime) {
        this.taskTime = taskTime;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getRemainTimes() {
        return remainTimes;
    }

    public void setRemainTimes(Integer remainTimes) {
        this.remainTimes = remainTimes;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", user=" + user +
                ", type=" + type +
                ", tags=" + tags +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", finishedTime=" + finishedTime +
                ", taskTime=" + taskTime +
                ", interval=" + interval +
                ", totalTimes=" + totalTimes +
                ", remainTimes=" + remainTimes +
                '}';
    }
}
