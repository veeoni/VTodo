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

任务：
1. 新增功能的完善
    - ~~新增待办时，(10 mins)~~
        - ~~特殊值的测试（-1时，次数设置为永久（65536），0时，设置为已完成，填写finishedDate）~~
    - ~~新增复习时，(30mins)~~
        - ~~EF、间隔等其他数值的确定~~
    - 添加成功的消息提示框
   

2. 编辑功能(3h)
    - 点击编辑时，出现编辑框，两种方案（降低耦合性，别把做好的功能又弄乱了）
        1. ~~借用原新增form（隐含域加上新增/修改以及各种乱七八糟的数据），方便确定怎么做~~
        2. 添加一个新的form，重新设计、重新编写表单验证、提交、局部刷新
            - 编辑时可逆向：当总次数为65535（永久）时，返回到页面时为-1（方便修改）(在服务端进行处理，getTodoById())
            - 规划：采用类似blog里的comment的方法，建立data域，保存更改需要的所有数据
            - 在js里进行读取
            - 修改controller，根据save方法，写一个edit方法
3. 完成功能(3h)
    - 针对待办
        - 点击完成，(加个提示框)，后台处理
        - 后台获得id即可，查询相应的数据，remainTimes--，判断是否为0？标记为完成 ：更新下一次出现的TaskDate
    - 针对复习
        - 点击完成，显示评价框
        - 用户进行评价，点击提交
        - 后台接收到评价信息以及id，查询信息，计算新EF，更新interval，设置下一次时间
4. 显示功能的完善（根据数据的不同特性，分为4类）(3h)
5. ~~删除功能~~
6. 根据日期查询todo(1h)

7. 当且仅当登录时，显示待办

8. 在顶部增加头像（点击登录）
9. 迁移登录功能（也可以叫新增）
10. 注册功能

## 复习任务安排算法介绍
### 首先介绍SM-2算法
- I(1):=1
- I(2):=6
- for n>2 I(n):=I(n-1)*EF
- E-Factor的范围是1.1~2.5，默认2.5（默认很熟悉）
- EF’:=EF-0.8+0.28*q-0.02*q*q
- 当q=4时，EF不改变
### 我的算法
1. 复习间隔Interval(double型)，当I参与下次日期计算时，仅取其下取整or四舍五入。 
   - 初始I=1，EF=2
   - 用户有3个选择，差(q=2) 中(q=4) 好(q=6)
2. 当I<6时,根据I计算新的I（I'）
   - 选差，I‘:=I+1
   - 选中，I‘:=I+2
   - 选好，I‘:=I+5
   
   再根据I'计算下一次复习时间,同时使用SM-2算法计算新的EF值（EF'）

3. 当I>=6时，先使用SM-2算法计算EF'，再使用EF’计算I‘，最后计算下一次复习时间




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
