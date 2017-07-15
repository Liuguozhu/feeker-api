package org.sidao.cm.node;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Node model.
 */
@SuppressWarnings("serial")
public class Node extends Model<Node> {
	public static final Node dao = new Node();
	
	/**
	 * 分页查询
	 */
	public Page<Node> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from node order by id desc");
	}
}
