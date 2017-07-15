package org.sidao.cm.common;

import org.apache.log4j.Logger;

import org.sidao.cm.node.Node;
import org.sidao.cm.role.Role;
import org.sidao.cm.users.Users;
import org.sidao.common.VerificationCode;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;

import java.util.ArrayList;
import java.util.List;

public class CommonController extends Controller {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(CommonController.class);

    /**
     * Index void.
     */
    @Clear
    public void index() {
        main();
    }

    @Clear
    public void managerIndex() {
        render("common/login.html");
    }

    /**
     * 客户端进去
     */
    public void main() {
        Users user = getSessionAttr("clientUser");
        List<Node> nodes ;
        if (user != null) {
            long userId = user.getLong("id");
            Role role = Role.dao.findFirst("SELECT * FROM role WHERE id=(SELECT role_id FROM user_roles WHERE user_id=?)", new Object[]{userId});
            if (role != null) {
                String roleName = role.getStr("name");
                int roleStatus = role.getInt("status");
                if ("Admin".equals(roleName)) {//管理员
                    nodes = Node.dao.find("SELECT * FROM node WHERE pid=0");
                    for (Node node : nodes) {
                        long nodeId = node.getLong("id");
                        List<Node> childNodes = Node.dao.find("SELECT * FROM node WHERE pid=?", new Object[]{nodeId});
                        node.put("child_nodes", childNodes);
                    }
                } else if (!"Admin".equals(roleName) && roleStatus == 2) {//角色启用但不是管理员
                    nodes = getNodesByRoleId(role.getLong("id"));
                } else {
                    nodes = getNodesByRoleId(3);
                }
            } else {
                nodes = getNodesByRoleId(3);
            }
        } else {//找出匿名权限的node
            int anonymousRoleId = 3;
            nodes = getNodesByRoleId(anonymousRoleId);
        }

        setAttr("user", user);
        setAttr("nodes", nodes);
        render("/front/index.html");
    }

    /**
     * 后台管理
     */
    public void managerMain() {
        Users user = getSessionAttr("user");
        long userId = user.getLong("id");
        List<Node> nodes ;

        Role role = Role.dao.findFirst("SELECT * FROM role WHERE id=(SELECT role_id FROM user_roles WHERE user_id=?)", new Object[]{userId});
        if (role == null) {
            nodes=getNodesByRoleId(3);//匿名用户
            setAttr("user", user);
            setAttr("nodes", nodes);
            render("/common/main.html");
            return;
        }
        int roleStatus = role.getInt("status");
        String roleName = role.getStr("name");
        long roleId = role.getLong("id");
        if ("Admin".equals(roleName)) {//管理员
            nodes = Node.dao.find("SELECT * FROM node WHERE pid=0 and id <5");
            for (Node node : nodes) {
                long nodeId = node.getLong("id");
                List<Node> childNodes = Node.dao.find("SELECT * FROM node WHERE pid=?", new Object[]{nodeId});
                node.put("child_nodes", childNodes);
            }
        } else if (roleStatus == 2) {//不是管理员，并且角色启用
            nodes = getNodesByRoleId(roleId);
        }else{
            nodes=getNodesByRoleId(3);//匿名用户
        }

        setAttr("user", user);
        setAttr("nodes", nodes);
        render("/common/main.html");
    }

    private List<Node> getNodesByRoleId(long roleId) {
        List<Node> nodes = new ArrayList<Node>();
        nodes = Node.dao.find("SELECT * FROM node WHERE pid=0 AND id IN  " +
                "( " +
                "SELECT node_id FROM node_roles WHERE role_id=? " +
                ") " +
                "OR id IN( " +
                "SELECT pid FROM node WHERE id IN  " +
                "( " +
                "SELECT node_id FROM node_roles WHERE role_id=? " +
                ") " +
                ")", new Object[]{roleId, roleId});
        for (Node node : nodes) {
            long nodeId = node.getLong("id");
            List<Node> childNodes = Node.dao.find("SELECT * FROM node WHERE pid=? AND id IN  " +
                    "( " +
                    "SELECT node_id FROM node_roles WHERE role_id=?" +
                    ") " +
                    "OR pid=? AND pid IN  " +
                    "( " +
                    "SELECT node_id FROM node_roles WHERE role_id=? " +
                    ")  ", new Object[]{nodeId, roleId, nodeId, roleId});
            node.put("child_nodes", childNodes);
        }
        return nodes;
    }

    @Clear
    public void random_code() {
        VerificationCode img = new VerificationCode("cmSystemRandomCode");
        render(img);
    }


}
