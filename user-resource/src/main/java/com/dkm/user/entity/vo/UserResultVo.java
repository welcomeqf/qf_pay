package com.dkm.user.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/13
 * @vesion 1.0
 **/
@Data
public class UserResultVo {

   /**
    *  token
    */
   private String token;

   /**
    * 过期时间
    */
   private String expTime;
}
