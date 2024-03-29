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
1. ~~note.html的目录切换
点击右下角“目录”按钮后，若弹出不了，则显示在去除js中的自定义toc容器~~
2. 设置功能中添加，使用editormd渲染或者commonmark渲染（将所有@{/note/{id}}的链接替换为/notes）
有个取巧的方法，提前把所有/note成/{note}(note=#{配置在messages.properties的note/notes})
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
    - ~~点击编辑时，出现编辑框，两种方案（降低耦合性，别把做好的功能又弄乱了）~~
        1. ~~借用原新增form（隐含域加上新增/修改以及各种乱七八糟的数据），方便确定怎么做~~
        2. ~~添加一个新的form，重新设计、重新编写表单验证、提交、局部刷新~~
            - ~~编辑时可逆向：当总次数为65535（永久）时，返回到页面时为-1（方便修改）(在服务端进行处理，getTodoById())~~
              （没改这个，还是给用户显示了65535，加入issue，有空再改）
            - ~~规划：采用类似note里的comment的方法，建立data域，保存更改需要的所有数据~~
            - ~~在js里进行读取~~
            - ~~修改controller，根据save方法，写一个edit方法~~(没写edit，共用了save方法)
            - ~~添加校验域~~
    - ~~测试编辑逻辑（当用户选择不重复后，totalTimes不做更改）~~
3. ~~完成功能(3h)~~
    - ~~针对待办~~
        - ~~点击完成，(加个提示框)，后台处理~~
        - ~~后台获得id即可，查询相应的数据，remainTimes--，判断是否为0？标记为完成 ：更新下一次出现的TaskDate~~
    - ~~针对复习~~
        - ~~点击完成，显示评价框~~
        - ~~用户进行评价，点击提交~~
        - ~~后台接收到评价信息以及id，查询信息，计算新EF，更新interval，设置下一次时间~~
4. 显示功能的完善（根据数据的不同特性，分为4类）(3h)
   - ~~后台~~
      - ~~将todoList加上两个标签，是否遗留+是否完成~~
   - ~~今日未完成中~~
      - ~~区分是否遗留~~
      - ~~区分待办与复习~~
   - 今日已完成中
      - ~~区分待办与复习~~
      - 今日已完成的待办的逻辑修改
         - 支持编辑
         - ~~支持删除~~
         - 支持撤销
   - 第x次的次数显示需要优化（有可能，在修改某待办的次数之后，显示不正常（从1开始计数）），两种方案
        - 增加第x次的数据项
        - 将完成逻辑整体修改
            - 把完成日期改成List(单独建一张表)
            - 每次完成，都只增加一个完成日期
            - 同时增加一个完成次数数据项（每次获取这个项，代表完成次数，去除remainTimes）
5. ~~删除功能~~
   - ~~删除已完成的待办~~
   - ~~删除未完成的待办~~
6. ~~根据日期查询todo(1h)~~
7. ~~当且仅当登录时，显示待办~~
8. ~~在顶部增加头像（点击登录）~~
   - ~~头像下增加个人中心，把原后台管理改成个人中心，~~/admin路径改为/(暂时不改路径，太麻烦，涉及到几十个页面)
9. ~~迁移登录功能（也可以叫新增登录功能）~~
10. ~~注册功能~~
11. 修改登录后的页面访问逻辑（跳转->重定向）
    - ~~访问"/"，判断是否登录，登录则进入todo，未登录，进入index~~
    - ~~访问"/todo"，未登录则跳转登录，登录了就正常处理~~
    - ~~访问“/login”，登录就跳转todo~~
    - 访问”/index及其他网页“，必须根据userid查询
      1. 路径是否有userid（每个Getmapping多增加一个value,/index/{userid}）
      2. 是否登录（session中,user.getId()）
      3. 默认id=1(站长)
    - ~~删除todo的底部推荐，这里是xxx的家等等~~
12. 个人信息更改
    - ~~头像更改~~(半成品，考虑填写网址or文件上传)
    - 为用户增加自定义设置
        - about页面的图片地址、显示的信息、标签
        - （可选）为每个人设置一个底部的信息栏
        - note页面的打赏图片的上传
        - 头像的上传
13. ~~为笔记填写页面增加默认图像，去除必填的校验域，不填就在保存数据库的时候加个默认的~~
14. 登录后自动跳转上一个页面
15. 登录与注册的验证码功能（调包侠）
16. ~~隐秘的BUG，复习条目可能出现EF不存在的情况~~
17. 新逻辑
    - 过去的页面，不允许进行编辑、完成的操作，只能删除
    - 对于任何编辑的页面，若taskData<=now，则查now，否则跳转到TaskDate
    - 对于删除，跳转到当前页面
    - 对于新增，仅可新增现在及未来的待办，跳转到新增的TaskDate页面
18. 遗留任务的总结    
    - 1.新增成功的消息提示框
    - 2.编辑时可逆向：当总次数为65535（永久）时，返回到页面时为-1（方便修改）(在服务端进行处理，getTodoById())
    - 4.今日已完成的待办的逻辑修改
        - 支持编辑（对已完成的多次待办、复习的编辑较难实现，可以选择建新表，增加一个派生ID，每次完成后，若需要插入，则塞到新表里）
        - 支持撤销
    - 4.第x次的次数显示需要优化
        - （有可能，在修改某待办的次数之后，显示不正常（从1开始计数）），两种方案
            - 增加第x次的数据项
            - 将完成逻辑整体修改
                - 把完成日期改成List(单独建一张表)
                - 每次完成，都只增加一个完成日期
                - 同时增加一个完成次数数据项（每次获取这个项，代表完成次数，去除remainTimes）
19. 增加所有字段最大长度、最小长度的限制和提示
20. 保存的文章也会被展示到首页，这就不对了。
21. 暂时解决不了的BUG，使用JPA通过TAG/TYPE查询note时，标签下有多少个note，需要用到tag.notes.size。此时所查的notes.size没有published=true的限制，因此会虚高
    - 暂时想到的解决方法：在Thymeleaf中获取了note之后，遍历，或者在Controller中对获取到的Tag进行进一步的操作。



4/21 周三
1. 访问逻辑更改（整个上午）
2. 验证码功能的开发 
3. ~~博客更名为笔记~~
4. 毕设任务书
    - 参考文献寻找
    - 外文寻找（进行翻译）
    
4/22 周四
1. 权限管理
2. 系统功能结构图
3. 倒计时功能
4. 目标功能
5. 显示在todo页面

4/23 周五
1. 周报
2. 页脚的信息更改
3. 关于我的重新设计（每个人都有，还是关于本站）
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

## 设置默认权限
insert into t_role (id, name)
values (1, '管理员'), (2, '普通用户');

insert into t_permission (id, name)
values (1, '登录'),(2, '日程管理'),(3, '笔记管理'),(4, '分类管理'),(5, '标签管理'),(6, '目标管理'),(7, '倒计时管理'),(8, '权限管理');

insert into t_permission_roles (roles_id, permissions_id)
values (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(2,1),(2,2),(2,3),(2,4),(2,5),(2,6),(2,7);