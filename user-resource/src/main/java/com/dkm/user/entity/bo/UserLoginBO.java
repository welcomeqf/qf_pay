package com.dkm.user.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/13
 * @vesion 1.0
 **/
@Data
public class UserLoginBO {

   /**
    * 用户名
    */
   private String userName;

   /**
    * 密码
    */
   private String password;
}
