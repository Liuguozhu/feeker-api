package org.sidao.cm.users;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * UsersInterceptor
 */
public class UsersInterceptor implements Interceptor {
	public final static String SESSION_NAME_USER="user";
	public final static String ADMIN="admin";
	@Override
	public void intercept(Invocation inv) {
		Users localUsers = inv.getController().getSessionAttr(SESSION_NAME_USER);
		
			if(localUsers.getStr("username")!=null){
				if(!localUsers.getStr("username").equals("admin")){
					return;
				}
			}
			
		
		// TODO Auto-generated method stub
		inv.invoke();
		
	}
}
