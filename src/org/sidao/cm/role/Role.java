package org.sidao.cm.role;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Role model.
 */
@SuppressWarnings("serial")
public class Role extends Model<Role> {
	public static final Role dao = new Role();
	
	/**
	 * 分页查询
	 */
	public Page<Role> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from role order by id desc");
	}

}
