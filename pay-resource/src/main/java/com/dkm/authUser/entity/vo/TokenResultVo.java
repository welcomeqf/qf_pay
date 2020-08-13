package com.dkm.authUser.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Data
public class TokenResultVo {

   /**
    * token
    */
   private String token;

   /**
    * 过期时间
    */
   private String expTime;
}
