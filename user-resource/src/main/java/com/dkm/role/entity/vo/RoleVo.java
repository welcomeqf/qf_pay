package com.dkm.role.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/12
 * @vesion 1.0
 **/
@Data
public class RoleVo {

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

   /**
    * 备注
    * 备用字段
    */
   private String remark;
}
