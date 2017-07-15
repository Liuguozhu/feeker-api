/**
 * 
 */
package test;

import org.sidao.cm.users.Users;
import org.sidao.jdbc.BonePlugin;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;

/**
 * @author wxc
 *
 */
public class TestUtils {
	public static void initDb(){
		BonePlugin bp=new BonePlugin();
		bp.start();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(bp);
		arp.setDialect(new MysqlDialect());
		arp.setShowSql(true);
		
        arp.addMapping("users", Users.class);
       
		arp.start();
	}
}
