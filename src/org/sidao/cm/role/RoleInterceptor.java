package org.sidao.cm.role;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * RoleInterceptor
 */
public class RoleInterceptor implements Interceptor {
	
	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		inv.invoke();
		
	}
}
