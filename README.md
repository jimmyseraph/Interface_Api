# Interface API
用于进行六道先生的接口测试课程实践使用，目前完成的接口：

## 1、SearchBookApi接口

​	SearchBook使用简单的Servlet实现，包含两个接口：login和sbook，login接口用于进行用户身份验证，需要提供用户名和密码，如果验证成功则返回json数据，其中包含一个1小时有效的token；sbook接口用于进行数据库中编程书籍数据的查询，必须提供token和用户名来验证身份，如果验证成功，则根据传递的筛选条件返回查询结果。

### 1.1、部署说明

​	运行容器是Tomcat8.5，Jdk为1.8，mysql数据库为5.7.19，数据库库名为book_store，表创建脚本存放在项目中的bookstore.sql，数据库连接信息存放在dbcp.properties文件中，可以自行修改。

### 1.2、login接口说明

**login接口访问路径：** http://localhost:8080/jwt_interface/login

**请求方法：** POST

**参数：**

​	user : liudao（目前仅支持这个用户）

​	pwd : 123456

*该请求并没有从数据库中进行实际的用户名和密码验证，而是在代码中写死了liudao和123456这一对用户名和密码，因为登录接口不是主要的应用接口，所以在此只是简单实现。

**返回：** JSON

​	如果验证成功，返回

​		status：状态

​		token：有效时间为1小时的token

​	如果验证失败，返回

​		errorCode：错误代码

​		errorMessage：错误信息

*如果验证成功，还会在客户端生成一个名为expir的cookie，值为一个过期时间的毫秒数表达式，cookie有效期同样为1小时。

### 1.3、sbook接口

**sbook接口访问路径：** http://localhost:8080/jwt_interface/sbook

**请求方法：** POST

**验证方式：** Bearer Token（header中需要有Authorization，值为Bearer token值）

**参数：**

​	user : 用户名（目前只有liudao）

​	id : 书籍的id，筛选条件之一，为整数，可省略

​	name : 书籍的名称，筛选条件之一，支持模糊查询，可省略

​	author : 书籍的作者，筛选条件之一，支持模糊查询，可省略

​	price : 书籍的价格，为double类型，筛选条件之一，表示不超过该价格，可省略

**Cookie：** login成功后保存的名为expir的cookie，如果缺少将报token过期的错误

**返回：**

​	如果请求成功，则返回：

​		count : 查询符合条件的书本数量

​		books :  符合条件的书本集合

​			bookId ：书籍id

​			bookName ： 书籍名称

​			bookAuthor ： 书籍作者

​			bookPrice ： 书籍价格 

​	如果请求没有成功，则返回：

​		errorCode：错误代码

​		errorMessage：错误信息
