package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MenuHandler {
    @Autowired
    private MenuService menuService;

    @ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew() {
        List<Menu> menuList = menuService.getAll();
        Menu root = null;
        HashMap<Integer, Menu> menuMap = new HashMap<>();
        for (Menu menu : menuList) {
            Integer id = menu.getId();
            menuMap.put(id, menu);
        }

        // map存储引用地址
        for (Menu menu : menuList) {
            Integer pid = menu.getPid();
            if (pid == null) {
                root = menu;
                continue;
            }

            menuMap.get(pid).getChildren().add(menu);
        }
        return ResultEntity.successWithData(root);
    }

    @ResponseBody
    @RequestMapping(value = "/menu/save.html", produces="application/json;charset=UTF-8")
    public ResultEntity<String> saveMenu(Menu menu) {
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu) {
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id) {
        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }

}
