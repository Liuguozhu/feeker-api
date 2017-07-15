package org.sidao.cm.users;

import com.jfinal.core.Controller;
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.validate.Validator;
import org.apache.commons.lang.StringUtils;

public class LoginValidator extends Validator {

    private static final String msg = "message";
	
	protected void validate(Controller c) {
		setShortCircuit(true);
		validateRequired("user_name", "user_nameMsg", "请您输入<strong>用户</strong>名!");
		validateRequired("password", "passwordMsg", "请您输入密码!");
		validateRequired("randomCode", "randomCodeMsg", "请您输入<strong>验证</strong>码!");
		
		String inputRandomCode = c.getPara("randomCode");
		if (StringUtils.isNotBlank(inputRandomCode))
			inputRandomCode = inputRandomCode.toUpperCase();
		if (CaptchaRender.validate(c,inputRandomCode, "cmSystemRandomCode") == false) {
			addError(msg, "<strong>验证</strong>码输入不正确,请重新输入!");
		}
	}
	
	protected void handleError(Controller c) {
		c.keepPara("user_name");
		c.redirect("/");;
	}
}