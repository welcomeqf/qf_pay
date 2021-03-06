package com.dkm.authUser.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("pay_auth")
public class AuthInfo extends Model<AuthInfo> {

   /**
    * 主键ID
    */
   private Long id;

   /**
    * appID
    */
   private String appId;

   /**
    * 设备名称
    */
   private String authName;

   /**
    * 设备描述
    */
   private String authDescribe;

   /**
    * 设备登录用户名
    */
   private String authUser;

   /**
    * 设备登录密码
    */
   private String authPassword;

   /**
    * 是否停用(0--正常·  1--停用)
    * 默认正常
    */
   private Integer isStopped;

   /**
    * 创建时间
    */
   @TableField(fill = FieldFill.INSERT)
   private LocalDateTime createDate;

   /**
    * 创建人
    */
   private String createUser;

   /**
    * 修改时间
    */
   @TableField(fill = FieldFill.INSERT_UPDATE)
   private LocalDateTime updateDate;

   /**
    * 修改人
    */
   private String updateUser;

   /**
    * 微信支付的商户ID
    */
   private String mchId;

   /**
    * 微信支付的密钥
    */
   private String paterNerKey;

   /**
    * 微信的AppID
    */
   private String wxAppId;
}
