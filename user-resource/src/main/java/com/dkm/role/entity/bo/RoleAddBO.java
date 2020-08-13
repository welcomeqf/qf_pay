package com.dkm.role.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Data
public class RoleAddBO {

   /**
    * 角色id
    */
   private Long id;

   /**
    * 角色名
    */
   private String roleName;

   /**
    *  0--默认 1--子用户
    */
   private Integer roleStatus;
}
