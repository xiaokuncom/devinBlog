-- 创建一个员工表
create table staff
(
    id   varchar(72)  not null comment '编号' primary key,
    name varchar(50)  null comment '姓名',
    sex  varchar(2)   null comment '性别',
    age  tinyint(100) null comment '年龄',
    data longblob     null comment '资料'
) engine = InnoDB
  default charset = utf8mb4
  row_format = dynamic;