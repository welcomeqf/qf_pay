package com.dkm.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Data
@TableName("user_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class User extends Model<User> {

   /**
    * 用户id
    */
   private Long id;

   /**
    * 设备Id
    */
   private Long authId;

   /**
    *  角色id
    */
   private Long userRoleId;

   /**
    * 公司id
    */
   private Long companyId;

   /**
    * 账号
    */
   private String account;

   /**
    * 密码(sha1加密)
    */
   private String password;

   /**
    *  微信openId
    */
   private String weChatOpenId;

   /**
    * 中文名
    */
   private String cName;

   /**
    * 英文名
    */
   private String eName;

   /**
    * 性别
    * 0--男 1--女
    */
   private Integer userSex;

   /**
    * 年龄
    */
   private Integer userAge;

   /**
    * 用户头像
    */
   private String userImgUrl;

   /**
    *  电话号码
    */
   private String userTel;

   /**
    * 身份证
    */
   private String userCard;

   /**
    * 用户级别
    *
    */
   private Integer userLevel;

   /**
    * 是否使用 0-使用 1--停用
    */
   private Integer isStopped;

   /**
    * 备注
    * 备用字段
    */
   private String remark;

   /**
    * 0--用户  1--子用户
    */
   private Integer userStatus;

   /**
    *  创建时间
    */
   private LocalDateTime createDate;

   /**
    * 创建人id
    */
   private Long createUserId;
}
