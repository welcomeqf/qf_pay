package com.dkm.type.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 设备表和登录方式的中间表
 * @author qf
 * @date 2020/8/10
 * @vesion 1.0
 **/
@Data
@TableName("auth_login_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AuthLogin extends Model<AuthLogin> {

   /**
    * 主键id
    */
   private Long id;

   /**
    * 设备id
    */
   private Long authProjectId;

   /**
    * 登录方式id
    */
   private Long loginId;
}
