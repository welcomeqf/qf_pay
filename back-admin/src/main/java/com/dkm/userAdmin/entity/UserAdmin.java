package com.dkm.userAdmin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qf
 * @date 2020/3/25
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("user_admin")
public class UserAdmin extends Model<UserAdmin> {

   private Long id;

   private String userName;

   private String userPassword;

   /**
    * 0--正常  1--禁用
    */
   private Integer status;
}
