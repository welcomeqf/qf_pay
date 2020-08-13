package com.dkm.type.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Data
public class AuthLoginVo {

   /**
    * 设备id
    */
   private Long authProjectId;

   /**
    * 项目名称
    */
   private String authName;

   /**
    * 登录方式id
    */
   private Long loginId;

   /**
    * 0--账号密码登录 1--微信登录
    * 后续还可以加
    */
   private Integer loginType;

   /**
    *  登录方式的名称
    */
   private String loginName;
}
