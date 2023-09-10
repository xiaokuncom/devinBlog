create table user (
      user_id int comment 'id' auto_increment primary key ,
      login_name varchar(50) comment '登录名',
      pwd varchar(255) comment '密码',
      name varchar(50) comment '姓名',
      age int comment '年龄',
      gender int(2) comment '性别'
)
