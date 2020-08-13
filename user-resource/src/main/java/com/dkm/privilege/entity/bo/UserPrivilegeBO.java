package com.dkm.privilege.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Data
public class UserPrivilegeBO {

   /**
    * 角色id
    */
   private Long roleId;

   /**
    *  菜单id集合
    */
   private List<Long> menuList;
}
