package com.dkm.user.entity.vo;

import lombok.Data;



/**
 * @author qf
 * @date 2020/8/13
 * @vesion 1.0
 **/
@Data
public class UserVo {

   /**
    * 用户id
    */
   private Long id;

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
    * 中文名(昵称)
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
}
