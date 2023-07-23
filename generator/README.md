# 本项目是一个基于Apache Velocity引擎开发的代码生成器。
# 使用须知
1.新增操作时，生成的sql忽略了主键id，所以请务必事先将mysql的主键id设置为主键自增
2.通过Postman访问 http://localhost:8888/gen/ddlFile 去生成代码时，请选择Body下form-data，务必传递键
为file，值为建表语句的SQL，其他字段可以不传。