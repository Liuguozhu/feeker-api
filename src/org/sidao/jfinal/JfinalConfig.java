package org.sidao.jfinal;


import org.sidao.cm.common.CommonController;
import org.sidao.cm.common.CommonInterceptor;
import org.sidao.cm.node.Node;
import org.sidao.cm.node.NodeController;
import org.sidao.cm.role.NodeRoles;
import org.sidao.cm.role.RoleController;
import org.sidao.cm.role.UserRoles;

import org.sidao.cm.role.Role;
import org.sidao.cm.users.Users;
import org.sidao.cm.users.UsersController;
import org.sidao.jdbc.BonePlugin;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.log.Log4jLoggerFactory;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.ehcache.EhCachePlugin;

/**
 * API引导式配置
 */
public class JfinalConfig extends JFinalConfig {
    /**
     * Logger for this class
     */
//	private static final Logger logger = Logger.getLogger(JfinalConfig.class);

    /**
     * 配置常量
     */
    public void configConstant(Constants me) {
        me.setDevMode(true);
        me.setLoggerFactory(new Log4jLoggerFactory());
    }

    /**
     * 配置路由
     */
    public void configRoute(Routes me) {
        me.add("/", CommonController.class);
        me.add("/users", UsersController.class);
        me.add("/role", RoleController.class);
        me.add("/node", NodeController.class);
    }

    /**
     * 配置插件
     */
    public void configPlugin(Plugins me) {
        //配置数据
        BonePlugin bp = new BonePlugin();
        me.add(bp);
        ActiveRecordPlugin arp = new ActiveRecordPlugin("base", bp);


        //配置第二个数据库
        //BonecpPlugin2 bp_wosdk=new BonecpPlugin2();
        //me.add(bp_wosdk);
        //ActiveRecordPlugin arp_wosdk = new ActiveRecordPlugin("wosdk",bp_wosdk);
        //arp_wosdk.setDialect(new MysqlDialect());
        //arp_wosdk.setShowSql(true);
        //me.add(arp_wosdk);

        //配置定时任务
//        QuartzPlugin qp = new QuartzPlugin();
//        me.add(qp);

        //配置redis
//		Prop redisProp=PropKit.use("RedisConnector.properties",Const.DEFAULT_ENCODING);
//		RedisPlugin jp= new RedisPlugin("defalut",redisProp.get("host"));
//		JedisPlugin jp= new JedisPlugin();
//		me.add(jp);
        //logger.info("配置redis数据源："+redisProp.get("host"));
        //配置ehcache
        me.add(new EhCachePlugin());

        arp.addMapping("user", "id", Users.class);
        arp.addMapping("role", "id", Role.class);
        arp.addMapping("node", "id", Node.class);
        arp.addMapping("user_roles", "id", UserRoles.class);
        arp.addMapping("node_roles", "id", NodeRoles.class);
        me.add(arp);
        arp.setDialect(new MysqlDialect());
        arp.setShowSql(false);
    }

    /**
     * 配置全局拦截
     */
    public void configInterceptor(Interceptors me) {
        me.add(new CommonInterceptor());
    }

    /**
     * 配置处理
     */
    public void configHandler(Handlers me) {

    }


}
