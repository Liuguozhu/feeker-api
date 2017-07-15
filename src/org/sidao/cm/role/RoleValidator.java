package org.sidao.cm.role;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * RoleValidator.
 */
public class RoleValidator extends Validator {
	
	protected void validate(Controller controller) {
        //validateRequiredString("role.", "Msg", "请输入Role标题!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Role.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/role/save"))
			controller.render("add.html");
		else if (actionKey.equals("/role/update"))
			controller.render("edit.html");
	}
}
