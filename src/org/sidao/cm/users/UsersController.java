package org.sidao.cm.users;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import org.sidao.cm.role.UserRoles;

/**
 * UsersController
 */
@Before(UsersInterceptor.class)
public class UsersController extends Controller {
    /**
     * Logger for this class
     */
    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(UsersController.class);

    public void index() {
        setAttr("usersPage", Users.dao.paginate(getParaToInt(0, 1), 10));
        render("users.html");
    }

    public void add() {
    }

    @Before(UsersValidator.class)
    public void save() {
        Users user = getModel(Users.class);
        user.set("create_time", new Date());
        user.set("last_login_time", new Date());
        user.save();
        redirect("/users");
    }

    public void show() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String username = getPara("NameLike");
        int pagesStart = getParaToInt("iDisplayStart") / getParaToInt("iDisplayLength") + 1;
        Page<Users> pages;
        if (!StringUtils.isBlank(username)) {
            pages = Users.dao.paginatebyname(pagesStart, getParaToInt("iDisplayLength"), "from user where remark is null and username like ? order by id asc", "%" + username + "%");
        } else {
            pages = Users.dao.paginate(pagesStart, getParaToInt("iDisplayLength"));
        }

        resultMap.put("list", pages.getList());
        resultMap.put("sEcho", getPara("sEcho"));
        resultMap.put("iTotalRecords", pages.getTotalPage());
        resultMap.put("iTotalDisplayRecords", pages.getTotalRow());
        renderJson(resultMap);
    }

    public void edit() {
        long userId = getParaToLong();
        setAttr("user", Users.dao.findById(userId));
        render("edit.html");
    }

    public void update() {
        Users user = getModel(Users.class);
        user.update();
        redirect("/users");
    }

    public void delete() {
        long id = getParaToLong();
        boolean deleteResult = false;
        if (1 != id)//admin不能删
            deleteResult = Users.dao.deleteById(id);
        if (deleteResult) //删除用户的所有角色
            deleteUserRole(id);

        redirect("/users");
    }

    public void groupdelete() {
        String userIds = getPara("userIds");
        String[] ids = userIds.split(",");
        for (int i = 1; i < ids.length; i++) {
            if (StringUtils.isBlank(ids[i])) {
                continue;
            }
            Integer userId = Integer.parseInt(ids[i]);
            boolean deleteResult = false;
            if (1 != userId)//admin不能删
                deleteResult = Users.dao.deleteById(userId);
            if (deleteResult) //删除用户的所有角色
                deleteUserRole(userId);

        }
        redirect("/users");
    }

    void deleteUserRole(long userId) {
        List<UserRoles> userRoles = UserRoles.dao.find("SELECT * FROM user_roles WHERE user_id=" + userId);
        for (UserRoles userRole : userRoles) {
            userRole.delete();
        }
    }

    @Clear
    @Before(LoginValidator.class)
    public void login() {
        String userName = getPara("user_name");
        String password = getPara("password");

        Users user = Users.dao.findFirst("select * from user where username=? and password=?", new Object[]{userName, password});
//        List<Users> userList= Users.dao.find("select * from user ");
        if (user == null) {
            setAttr("message", "用户名或者密码错误！");
            render("/common/login.html");
        } else {
            setSessionAttr("user", user);
            user.set("last_login_time", new Date());
            user.update();
            redirect("/main");
        }
    }

    @Clear
    @Before(LoginValidator.class)
    public void managerLogin() {
        String userName = getPara("user_name");
        String password = getPara("password");

        Users user = Users.dao.findFirst("select * from user where username=? and password=?", new Object[]{userName, password});
//        List<Users> userList= Users.dao.find("select * from user ");
        if (user == null) {
            setAttr("message", "用户名或者密码错误！");
            render("/common/login.html");
        } else {
            setSessionAttr("user", user);
            user.set("last_login_time", new Date());
            user.update();
            redirect("/managerMain");
        }
    }

    @Clear
    public void logout() {
        setSessionAttr("user", null);
        redirect("/");
    }

    /**
     * Validate name.验证用户名是否存在
     */
    public void validateName() {
        String loginName = getPara("loginName");
        String res = Users.dao.valitdateName(loginName);
        renderText(res);
    }
}


