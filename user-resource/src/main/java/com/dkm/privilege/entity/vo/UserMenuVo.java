package com.dkm.privilege.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/12
 * @vesion 1.0
 **/
@Data
public class UserMenuVo {

   /**
    * 菜单Id
    */
   private Long id;

   /**
    * 菜单名
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
    * 图标颜色
    */
   private String iconColor;
}
