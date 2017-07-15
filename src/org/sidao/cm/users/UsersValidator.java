package org.sidao.cm.users;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * UsersValidator.
 */
public class UsersValidator extends Validator {
	
	protected void validate(Controller controller) {
        //validateRequiredString("users.", "Msg", "请输入Users标题!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Users.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/users/save"))
			controller.render("add.html");
		else if (actionKey.equals("/users/update"))
			controller.render("edit.html");
	}
}
