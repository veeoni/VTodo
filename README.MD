# *VTodo*
本项目为 南京信息工程大学 应用技术学院 2017级 计算机科学与技术 专业 2班同学 王友宁 的毕业设计。 于2021年4月完成。
### 开发环境：
1. IntelliJ IDEA 2019.2.4 (Ultimate Edition) UTF-8文字编码
2. jdk1.8.0_231
3. MySQL 5.7.33
### 引用包：
1. 
### 项目结构：
1. 
### 功能
1. 
### 参考资料：
1. 
### *TODO*
1. blog.html的目录切换
点击右下角“目录”按钮后，若弹出不了，则显示在去除js中的自定义toc容器
2. 设置功能中添加，使用editormd渲染或者commonmark渲染（将所有@{/blog/{id}}的链接替换为/blogs）
有个取巧的方法，提前把所有/blog成/{blog}(blog=#{配置在messages.properties的blog/blogs})
用这种方法的话，可以写3个原型页面，第一个commonmark 第二个editor自定义TOC容器 第三个editor取消自定义容器。
3. 更改所有图片、头像为自己选的，放到图床上。

###关于todo页面的需求分析
1. 用户可以查看当日待办（根据当天的日期查询）
2. 用户可以查看任一天的待办（日期选择器）
3. 用户可以查看前一天和后一天的待办（两个按钮）
4. 在任一项待办列表为空时，显示“恭喜！今日待办全部完成”“今天还没有完成任何待办哦~“
5. 待办的分类为 待复习 待办


某日已完成的待复习

| stateName | value |
| ------- | ------ |
| finishedTime | `ThatDay()` |
| status | finished |
| type | review |

某日已完成的待办

| stateName | value |
| ------- | ------ |
| finishedTime | `ThatDay()` |
| status | finished |
| type | todo |

某日未完成的待复习

| stateName | value |
| ------- | ------ |
| finishedTime |  |
| status | doing |
| type | review |
| startTime | \<`ThatDay()` |
| endTime | \>`ThatDay()` |
| taskTime | ==`ThatDay()` |

某日遗留的未完成的待复习

| stateName | value |
| ------- | ------ |
| finishedTime |  |
| finished | false |
| type | review |
| startTime | \<`ThatDay()` |
| endTime | \<`ThatDay()` |
| taskTime | \<`ThatDay()` |

某日未完成的待办

| stateName | value |
| ------- | ------ |
| finishedTime |  |
| finished | false |
| type | todo |
| startTime | \<`ThatDay()` |
| endTime | \>`ThatDay()` |

某日遗留的未完成的待办

| stateName | value |
| ------- | ------ |
| finishedTime |  |
| finished | false |
| type | todo |
| startTime | \<`ThatDay()` |
| endTime | \<`ThatDay()` |
| taskTime | \<`ThatDay()` |
