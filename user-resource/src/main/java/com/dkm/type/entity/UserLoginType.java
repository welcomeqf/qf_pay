package com.dkm.type.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qf
 * @date 2020/8/10
 * @vesion 1.0
 **/
@Data
@TableName("user_login_type")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserLoginType extends Model<UserLoginType> {

   /**
    * 主键id
    */
   private Long id;

   /**
    * 0--账号密码登录 1--微信登录
    * 后续还可以加
    */
   private Integer loginType;

   /**
    *  登录方式的名称
    */
   private String loginName;

   /**
    * 0--使用  1--不使用
    */
   private Integer loginStatus;
}
