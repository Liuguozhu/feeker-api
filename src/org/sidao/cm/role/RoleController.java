package org.sidao.cm.role;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;
import org.apache.commons.lang3.StringUtils;
import org.sidao.cm.node.Node;
import org.sidao.cm.users.Users;

/**
 * RoleController
 */
public class RoleController extends Controller {
    public void index() {
        setAttr("rolePage", Role.dao.paginate(getParaToInt(0, 1), 10));
        render("role.html");
    }

    public void add() {
    }

    public void show() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int pagesStart = getParaToInt("iDisplayStart") / getParaToInt("iDisplayLength") + 1;
        Page<Role> pages = Role.dao.paginate(pagesStart, getParaToInt("iDisplayLength"));
        resultMap.put("list", pages.getList());
        resultMap.put("sEcho", getPara("sEcho"));
        resultMap.put("iTotalRecords", pages.getTotalPage());
        resultMap.put("iTotalDisplayRecords", pages.getTotalRow());
        renderJson(resultMap);
    }

    @Before(RoleValidator.class)
    public void save() {
        getModel(Role.class).save();
        redirect("/role");
    }

    public void edit() {
        setAttr("role", Role.dao.findById(getParaToInt()));
    }

    @Before(RoleValidator.class)
    public void update() {
        getModel(Role.class).update();
        redirect("/role");
    }

    public void delete() {
        long id = getParaToLong();
        boolean deleteResult = false;
        if (1 != id && 3 != id)//1是admin，3是匿名用户权限,不允许删除，否则会导致页面没数据
            deleteResult = Role.dao.deleteById(id);
        if (deleteResult) {
            deleteNodeRole(id);
            deleteUserRole(id);
        }

        redirect("/role");
    }

    /**
     * 删除role下的用户列表
     *
     * @param roleId roleId
     */
    private void deleteUserRole(long roleId) {
        List<UserRoles> userRoles = UserRoles.dao.find("SELECT * FROM user_roles WHERE role_id=" + roleId);
        for (UserRoles userRole : userRoles) {
            userRole.delete();
        }
    }

    /**
     * 删除role下的node授权
     *
     * @param roleId roleId
     */
    private void deleteNodeRole(long roleId) {
        List<NodeRoles> nodeRoles = NodeRoles.dao.find("SELECT * FROM node_roles where role_id=" + roleId);
        for (NodeRoles nodeRole : nodeRoles) {
            nodeRole.delete();
        }
    }


    /**
     * 去授权
     */
    public void accessToNode() {
        int roleId = getParaToInt();

        List<Node> nodeList = Node.dao.find("SELECT * FROM node WHERE pid=0");
        for (Node node : nodeList) {
            long nodeId = node.getLong("id");
            checkNode(node, nodeId, roleId);
            List<Node> childNodes = Node.dao.find("SELECT * FROM node WHERE pid=?", new Object[]{nodeId});
            node.put("child_nodes", childNodes);
            for (Node childNode : childNodes) {
                long childNodeId = childNode.getLong("id");
                checkNode(childNode, childNodeId, roleId);
            }
        }
        setAttr("nodeList", nodeList);
        setAttr("roleId", roleId);
        render("access.html");
    }

    void checkNode(Node node, long nodeId, int roleId) {
        String sql = "SELECT * FROM node_roles WHERE node_id=? AND role_id=?";
        NodeRoles userRole = NodeRoles.dao.findFirst(sql, new Object[]{nodeId, roleId});
        if (userRole != null) {
            node.put("isThisRole", true);
        } else {
            node.put("isThisRole", false);
        }
    }

    /**
     * 给当前角色添加页面授权
     */
    public void addAccess() {
        int roleId = getParaToInt("roleId");
        if (1 != roleId) {
            String sql = "SELECT * FROM node_roles where role_id=?";
            List<NodeRoles> nodeRolesList = NodeRoles.dao.find(sql, new Object[]{roleId});
            String nodeIds = getPara("nodeId");
            System.out.println("nodeIds=" + nodeIds);
            if (StringUtils.isBlank(nodeIds)) {
                removeAllNodeRole(roleId, nodeRolesList);
            } else {
                updateNodeRoles(roleId, nodeIds, nodeRolesList);
            }
        }
        redirect("/role");
    }

    private void removeAllNodeRole(long roleId, List<NodeRoles> nodeRolesList) {
        for (NodeRoles nodeRole : nodeRolesList) {
            nodeRole.delete();
        }
    }

    private void updateNodeRoles(long roleId, String nodeIds,
                                 List<NodeRoles> nodeRolesList) {
        String nodeIdArrays[] = nodeIds.split(",");
        for (NodeRoles nodeRole : nodeRolesList) {
            long nodeId = nodeRole.getLong("node_id");
            boolean isDelete = true;
            for (int i = 0; i < nodeIdArrays.length; i++) {
                if (nodeIdArrays[i] == null || StringUtils.isBlank(nodeIdArrays[i]))
                    continue;
                if (nodeId == Integer.parseInt(nodeIdArrays[i].trim())) {
                    isDelete = false;
                    break;
                }
            }
            if (isDelete)
                nodeRole.delete();
        }

        for (int i = 0; i < nodeIdArrays.length; i++) {
            if (nodeIdArrays[i] == null || StringUtils.isBlank(nodeIdArrays[i]))
                continue;
            boolean isAdd = true;
            for (NodeRoles nodeRole : nodeRolesList) {
                long nodeId = nodeRole.getLong("node_id");
                if (nodeId == Integer.parseInt(nodeIdArrays[i].trim())) {
                    isAdd = false;
                    break;
                }
            }
            if (isAdd) {
                int nodeId = Integer.parseInt(nodeIdArrays[i].trim());
                addNodeRole(roleId, nodeId);
            }
        }
    }

    private void addNodeRole(long roleId, long nodeId) {
        String sql = "SELECT * FROM node_roles WHERE role_id ="
                + roleId + " AND node_id=" + nodeId;
        NodeRoles nodeRole = NodeRoles.dao.findFirst(sql);
        if (nodeRole == null) {
            nodeRole = new NodeRoles();
            nodeRole.set("node_id", nodeId);
            nodeRole.set("role_id", roleId);
            nodeRole.save();
        }
    }

    /**
     * 用户列表
     */
    public void roleToUserList() {
        int roleId = getParaToInt();

        List<Users> usersList = Users.dao.find("SELECT * FROM `user`");
        for (Users user : usersList) {
            long userId = user.getLong("id");
            String sql = "SELECT * FROM user_roles WHERE user_id=? AND role_id=?";
            UserRoles userRole = UserRoles.dao.findFirst(sql, new Object[]{userId, roleId});
            if (userRole != null) {
                user.put("isThisRole", true);
            } else {
                user.put("isThisRole", false);
            }
        }
        setAttr("roleId", roleId);
        setAttr("userList", usersList);
        render("user_roles.html");
    }

    /**
     * 将选中用户用户添加到当前角色列表
     */
    public void addRoleToUser() {
        int roleId = getParaToInt("roleId");
        String sql = "SELECT * FROM user_roles where role_id=?";
        List<UserRoles> userRolesList = UserRoles.dao.find(sql, new Object[]{roleId});
        String userIds = getPara("userId");
        System.out.println("userIds=" + userIds);
        if (StringUtils.isBlank(userIds)) {
            removeAllUserRole(roleId, userRolesList);
        } else {
            updateUserRoles(roleId, userIds, userRolesList);
        }
        redirect("/role");
    }

    private void removeAllUserRole(long roleId, List<UserRoles> userRolesList) {
        for (UserRoles userRole : userRolesList) {
            if (roleId == 1)
                if (userRole.getLong("user_id") != 1)//admin 的 角色不删除
                    userRole.delete();
        }
    }

    private void updateUserRoles(long roleId, String userIds,
                                 List<UserRoles> userRolesList) {
        String userIdArrays[] = userIds.split(",");
        for (UserRoles userRole : userRolesList) {
            long userId = userRole.getLong("user_id");
            boolean isDelete = true;
            for (int i = 0; i < userIdArrays.length; i++) {
                if (userIdArrays[i] == null || StringUtils.isBlank(userIdArrays[i]))
                    continue;
                if (userId == Integer.parseInt(userIdArrays[i].trim()) || (1 == userId && roleId == 1)) {//admin角色的admin用户不删除
                    isDelete = false;
                    break;
                }
            }
            if (isDelete)
                userRole.delete();
        }

        for (int i = 0; i < userIdArrays.length; i++) {
            if (userIdArrays[i] == null || StringUtils.isBlank(userIdArrays[i]))
                continue;
            boolean isAdd = true;
            for (UserRoles c2a : userRolesList) {
                long userId = c2a.getLong("user_id");
                if (userId == Integer.parseInt(userIdArrays[i].trim())) {
                    isAdd = false;
                    break;
                }
            }
            if (isAdd) {
                int userId = Integer.parseInt(userIdArrays[i].trim());
                addUserRole(roleId, userId);
            }
        }
    }

    private void addUserRole(long roleId, long userId) {
        String sql = "SELECT * FROM user_roles WHERE role_id ="
                + roleId + " AND user_id=" + userId;
        UserRoles userRole = UserRoles.dao.findFirst(sql);
        if (userRole == null) {
            userRole = new UserRoles();
            userRole.set("user_id", userId);
            userRole.set("role_id", roleId);
            userRole.set("GatewayId", 0);
            userRole.save();
        }
    }
}


