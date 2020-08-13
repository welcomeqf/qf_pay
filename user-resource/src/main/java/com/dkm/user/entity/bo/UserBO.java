package com.dkm.user.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/13
 * @vesion 1.0
 **/
@Data
public class UserBO {

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
    * 中文名
    */
   private String cName;

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
    * 身份证
    */
   private String userCard;

   /**
    * 是否使用 0-使用 1--停用
    */
   private Integer isStopped;

   /**
    * 0--用户  1--子用户
    */
   private Integer userStatus;
}
