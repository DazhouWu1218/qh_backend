package com.njht.webyun.utils;

import com.njht.webyun.entity.Tree;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author daiguojun
 * @date 2021-07-13 22:29
 * 构建树结构
 */
public class TreeBuilderUtil {
    /**
     * 根节点父id默认为0,查询集合中所有的跟节点信息
     *
     * @param treeNodes
     * @return
     */
    public  static <T extends Tree>  List<T> buildTreeList(List<T> treeNodes) {
        List<T> levelOneMenuList = treeNodes.stream().filter(
                // 过滤出所有一级分类信息
                item -> Objects.equals("0", item.getParentId())
        ).map(item -> {
            //递归关联子分类信息
            item.setChildren(getChildrenListInfo(item, treeNodes));
            return item;
        }).collect(Collectors.toList());
        return levelOneMenuList;
    }

    /**
     * 递归查找指定分类的所有子分类信息
     *
     * @param currentMenu
     * @param treeNodes
     */
    private static <T extends Tree> List<T> getChildrenListInfo(T currentMenu, List<T> treeNodes) {
        List<T> childrenList = treeNodes.stream().filter(
                //过滤出当前菜单所匹配的子菜单信息  currentMenu.getId = Tree.getParentId
                item -> Objects.equals(currentMenu.getValue(), item.getParentId())
        ).map(menu -> {
                    //递归查找子节点信息
                    menu.setChildren(getChildrenListInfo(menu, treeNodes));
                    return menu;
                }
        ).collect(Collectors.toList());
        return childrenList;
    }


}
