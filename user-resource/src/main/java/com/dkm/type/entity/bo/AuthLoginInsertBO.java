package com.dkm.type.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Data
public class AuthLoginInsertBO {

   /**
    * 主键id
    */
   private Long id;

   /**
    * 设备id
    */
   private Long authProjectId;

   /**
    * 登录方式id
    */
   private Long loginId;
}
