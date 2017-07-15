package org.sidao.cm.node;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * NodeValidator.
 */
public class NodeValidator extends Validator {
	
	protected void validate(Controller controller) {
        //validateRequiredString("node.", "Msg", "请输入Node标题!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Node.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/node/save"))
			controller.render("add.html");
		else if (actionKey.equals("/node/update"))
			controller.render("edit.html");
	}
}
