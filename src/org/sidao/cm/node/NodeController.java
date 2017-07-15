package org.sidao.cm.node;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;
import org.sidao.cm.role.NodeRoles;

/**
 * NodeController
 */
public class NodeController extends Controller {
    public void index() {
        setAttr("nodePage", Node.dao.paginate(getParaToInt(0, 1), 10));
        render("node.html");
    }

    public void add() {
    }

    public void show() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int pagesStart = getParaToInt("iDisplayStart") / getParaToInt("iDisplayLength") + 1;
        Page<Node> pages = Node.dao.paginate(pagesStart, getParaToInt("iDisplayLength"));
        resultMap.put("list", pages.getList());
        resultMap.put("sEcho", getPara("sEcho"));
        resultMap.put("iTotalRecords", pages.getTotalPage());
        resultMap.put("iTotalDisplayRecords", pages.getTotalRow());
        renderJson(resultMap);
    }

    @Before(NodeValidator.class)
    public void save() {
        Node node = getModel(Node.class);
        if (node.get("name") == null)
            node.set("name", "");
        node.save();
        redirect("/node");
    }

    public void edit() {
        setAttr("node", Node.dao.findById(getParaToInt()));
    }

    @Before(NodeValidator.class)
    public void update() {
        Node node = getModel(Node.class);
        if (node.get("name") == null)
            node.set("name", "");
        node.update();
        redirect("/node");
    }

    public void delete() {
        long id = getParaToLong();
        boolean deleteResult = false;
        if (id > 10)//只有id大于10的才允许删除，要不然看不到用户管理和节点管理了
            deleteResult = Node.dao.deleteById(id);
        if (deleteResult)
            deleteNodeRole(id);
        redirect("/node");
    }

    private void deleteNodeRole(long nodeId) {
        List<NodeRoles> nodeRoles = NodeRoles.dao.find("SELECT * FROM node_roles where node_id=" + nodeId);
        for (NodeRoles nodeRole : nodeRoles) {
            nodeRole.delete();
        }
    }
}


