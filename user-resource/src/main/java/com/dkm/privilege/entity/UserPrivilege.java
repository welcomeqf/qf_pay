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
@TableName("user_privilege_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserPrivilege extends Model<UserPrivilege> {

   /**
    * 权限id
    */
   private Long id;

   /**
    * 设备Id
    */
   private Long authId;

   /**
    * 角色id
    */
   private Long userRoleId;

   /**
    * 菜单id
    */
   private Long userMenuId;
}
