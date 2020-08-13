package com.dkm.role.entity;

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
@TableName("user_role_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Role extends Model<Role> {

   /**
    * 角色id
    */
   private Long id;

   /**
    * 设备Id
    */
   private Long authId;

   /**
    * 角色名
    */
   private String roleName;

   /**
    *  0--使用 1--不使用
    */
   private Integer isStopped;

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
