package com.ven.vtodo.po;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flag;//复习 or 待办
    private String title;
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//th传值时，需要的是String，此处是Date，类型不匹配，所以要转换
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")//th传值时，需要的是String，此处是Date，类型不匹配，所以要转换
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date finishedDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date taskDate;
    @Column(name = "m_interval")
    private Double interval;
    private Integer totalTimes;
    private Integer remainTimes;
    private Double easinessFactor;

    @Transient//不保存至数据库
    private String tagIds;
    @Transient//不保存至数据库
    private Boolean repeat;
    // TODO 前端编辑之后，这两项数据未传递到后台，请注意
    @Transient//不保存至数据库
    private Boolean isRemain;
    @ManyToOne
    private User user;
    @ManyToOne
    private Type type;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags;

    public Todo() {
    }

    public Boolean isRemain() {
        return isRemain;
    }

    public void setRemain(Boolean remain) {
        isRemain = remain;
    }


    public void setRemain(boolean remain) {
        isRemain = remain;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Double getEasinessFactor() {
        return easinessFactor;
    }

    public void setEasinessFactor(Double easinessFactor) {
        this.easinessFactor = easinessFactor;
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

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public Date getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(Date taskDate) {
        this.taskDate = taskDate;
    }

    public Double getInterval() {
        return interval;
    }

    public void setInterval(Double interval) {
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

    public Boolean getRepeat() {
        if(this.flag.equals("复习")){
            return false;
        }
        if(this.repeat==null){
            this.repeat = this.totalTimes != 1;
        }
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public String getTagIds() {
        init();
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags) {
        if (tags!=null && !tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", flag='" + flag + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", finishedDate=" + finishedDate +
                ", taskDate=" + taskDate +
                ", interval=" + interval +
                ", totalTimes=" + totalTimes +
                ", remainTimes=" + remainTimes +
                ", easinessFactor=" + easinessFactor +
                ", tagIds='" + tagIds + '\'' +
                ", repeat=" + repeat +
                ", user=" + (user!=null?user.getId():"null") +
                ", type=" + (type!=null?type.getId():"null") +
//                ", tags=" + (tags.size()>0?tags.get(0).getId():"null") +
                '}';

    }
}
