package org.sidao.cm.node;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * NodeInterceptor
 */
public class NodeInterceptor implements Interceptor {
	
	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		inv.invoke();
		
	}
}
