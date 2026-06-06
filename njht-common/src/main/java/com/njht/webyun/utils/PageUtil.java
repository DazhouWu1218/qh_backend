package com.njht.webyun.utils;


import com.github.pagehelper.page.PageMethod;

import java.util.List;

/**
 * @author ：tianjm
 * @date ：Created in 2020/5/6 10:10
 * @description：自定义分页工具
 * @modified By：
 * @version: $
 */
public class PageUtil<T> {

    /**
     * 自定义List分页工具
     * @param list
     * @param pageNum 页码
     * @param pageSize 每页多少条数据
     * @return
     */
    public static<T> List startPage(List<T> list, Integer pageNum,
                                 Integer pageSize) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }
        // 记录总数
        Integer count = list.size();
        // 页数
        Integer pageCount = 0;
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        // 开始索引
        int fromIndex = 0;
        // 结束索引
        int toIndex = 0;

        //是否为最后一页
        if (!pageNum.equals(pageCount)) {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = fromIndex + pageSize;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = count;
        }
        List pageList = list.subList(fromIndex, toIndex);
        return pageList;
    }


    /**
     * 开启分页
     * @param pageParam
     * @param sizeParam
     * @param defaultPageValue
     * @param defaultSizeValue
     */
    public static void setPageAndSize(Integer pageParam,Integer sizeParam,Integer defaultPageValue,Integer defaultSizeValue){
        Integer page = pageParam == null || pageParam == 0 ? defaultPageValue:pageParam;
        Integer size = sizeParam == null || sizeParam == 0 ? defaultSizeValue:sizeParam;
        PageMethod.startPage(page,size);
    }

    /*public static List<T>  objTSysUser (List<Object> o) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<T> userList = new ArrayList<T>();
        *//*将List<Object> 转成 List<SysUser>*//*
        for (int i = 0; i < o.size(); i++) {
            SysUser user = new SysUser();
            SysUser sysUser = (SysUser)o.get(i);
            userList.add(sysUser);
        }

        *//*对LIST按照lastLoginDate倒叙排 20200512*//*
        List<SysUser> list = userList.stream()
                .sorted(Comparator.comparing(SysUser::getLastLoginDate).reversed())
                .collect(Collectors.toList());

        return list;
    }*/


}
