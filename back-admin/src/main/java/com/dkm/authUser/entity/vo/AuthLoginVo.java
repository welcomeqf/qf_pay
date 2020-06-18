package com.dkm.authUser.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Data
public class AuthLoginVo {

   /**
    * 设备账号
    */
   private String authUserKey;

   /**
    * 密码
    */
   private String authPassword;
}
