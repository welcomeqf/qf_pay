package com.dkm.wxpay.entity;

import lombok.Data;

/**
 * 微信APP支付返回给前端的数据
 * @author qf
 * @date 2020/7/21
 * @vesion 1.0
 **/
@Data
public class WxPayResultVo {

   /**
    * 微信支付AppId
    */
   private String appId;

   /**
    * 时间戳
    */
   private String timeStamp;

   /**
    *  微信支付随机数
    */
   private String nonceStr;

   /**
    *  加密类型
    */
   private String signType;

   /**
    * 微信预支付id
    */
   private String prepayId;

   /**
    * 微信支付密钥
    */
   private String paySign;
}
