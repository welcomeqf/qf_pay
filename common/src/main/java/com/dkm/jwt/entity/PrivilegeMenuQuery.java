package com.dkm.jwt.entity;

import lombok.Data;


/**
 * @Author qf
 * @Date 2019/9/19
 * @Version 1.0
 */
@Data
public class PrivilegeMenuQuery {

    /**
     * 菜单Id
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 链接地址
     */
    private String action;

    /**
     * 图标路径
     */
    private String iconClass;

    /**
     *  图标颜色
     */
    private String iconColor;

    /**
     * 父id
     */
    private Long parent;
}
