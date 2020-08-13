package com.dkm.authUser.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Data
public class AuthRegisterVo {

   /**
    * 主键id
    */
   private Long id;


   /**
    * 设备id
    */
   private Long authProjectId;

   /**
    * 支付宝的appID
    */
   private String appId;

   /**
    * 微信支付的商户ID
    */
   private String mchId;

   /**
    * 微信支付的密钥
    */
   private String paterNerKey;

   /**
    * 微信的AppID
    */
   private String wxAppId;

   /**
    * 是否停用(0--正常·  1--停用)
    * 默认正常
    */
   private Integer isStopped;
}
