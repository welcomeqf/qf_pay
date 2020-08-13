package com.dkm.privilege.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/12
 * @vesion 1.0
 **/
@Data
public class MenuResultVo {

   /**
    *  菜单id
    */
   private Long id;

   /**
    * 菜单名称
    */
   private String menuName;

   /**
    * 0--有此权限
    * 1--无此权限
    */
   private Integer start;

   /**
    * 父id
    */
   private Long parentId;

   /**
    *  该父权限下的子权限
    */
   private List<UserMenuPrivilegeVo> list;
}
