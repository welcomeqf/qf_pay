package com.dkm.type.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/10
 * @vesion 1.0
 **/
@Data
public class UserLoginTypeBO {

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
}
