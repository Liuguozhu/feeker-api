package org.sidao.cm.users;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;


/**
 * Users model.
 */
@SuppressWarnings("serial")
public class Users extends Model<Users> {
	public static final Users dao = new Users();
	
	/**
	 * 分页查询
	 */
	public Page<Users> paginate(int pageNumber, int pageSize) {
		return paginate(pageNumber, pageSize, "select *", "from user order by id asc");
	}
	public Page<Users> paginatebyname(int pageNumber, int pageSize, String sql,String name) {
		return paginate(pageNumber, pageSize, "select *", sql,name);
	}

    /**
     * Valitdate name.验证用户名是否存在
     *
     * @param loginName the loginname
     * @return the string 0有重复，不可以添加 1可以
     */
    public String valitdateName(String loginName){
        StringBuffer sql=new StringBuffer("select * from user where username=? ");
        Users users= Users.dao.findFirst(sql.toString(),new Object[]{loginName});
        if(users!=null){
            return "0";
        }else{
            return "1";
        }
    }

	@Override
	public String toString() {
		return this.getStr("loginname");
	}
    
}
