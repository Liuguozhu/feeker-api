#feeker-api
环境要求JDK1.6以上。
本项目基于jfinal框架。jfinal使用及相关问题见官方文档。
上面两个目录jfinal2和jfinalext为jfinal部分源码，可忽略。
WebRoot/WEB-INF/lib下有jar包。需要build path

doc目录下为sql脚本。一个为数据库结构，另一个为初始化数据。

resources为配置文件目录，数据库的账号密码为加密后的字符串，加密规则及示例见src/org.sidao.jdbc.CipherUtils.java。

无需配置数据库 将src/org.sidao.jfinal.JfinalConfig.java中的数据库配置相关代码注释掉或删除即可（默认已注释掉，可直接启动）。

tools目录为自写的一个自动生成jfinal框架的bean和controlles的工具及模板。可根据实际需求自行更改。

启动类 test/RunProject.java。
直接main方法启动。启动参数可自行修改。参数意义详见jfinal源码或官方文档。

启动后访问http://localhost:8080/

后台管理http://localhost:8080/managerIndex 账号密码均为admin，详见数据库user表。
可管理用户，角色和权限。不同角色可以看到不同的api节点。

和swagger-to-feeker-api项目关联，通过swagger-to-feeker-api可以动态的将swagger的api动态添加到找个api项目中。
在权限中添加即可看到对应的api。


