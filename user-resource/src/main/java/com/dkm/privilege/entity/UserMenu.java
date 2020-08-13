package com.dkm.privilege.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Data
@TableName("user_menu_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserMenu extends Model<UserMenu> {

   /**
    * 菜单Id
    */
   private Long id;

   /**
    * 设备Id
    */
   private Long authId;

   /**
    * 父id
    */
   private Long parentId;

   /**
    * 菜单名
    */
   private String menuName;

   /**
    * 链接地址
    */
   private String action;

   /**
    * 排序
    */
   private Integer sort;

   /**
    * 图标路径
    */
   private String iconClass;

   /**
    * 图标颜色
    */
   private String iconColor;

   /**
    * 0-系统菜单 1--不是系统菜单
    */
   private Integer menuSystemic;

   /**
    * 0--使用 1-停用
    */
   private Integer menuStopped;
}
