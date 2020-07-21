package com.dkm.wxpay.entity;

import lombok.Data;

/**
 * @author qf
 * @date 2020/7/21
 * @vesion 1.0
 **/
@Data
public class WxJsApiPayResultVo {

   /**
    * 微信支付AppId
    */
   private String appId;

   /**
    *  商户id
    */
   private String mchId;

   /**
    * 时间戳
    */
   private String timeStamp;

   /**
    *  签名
    */
   private String sign;

   /**
    *  微信支付随机数
    */
   private String nonceStr;

   /**
    *  加密类型
    */
   private String signType;

   /**
    *  ---
    */
   private String tradeType;

   /**
    * 微信预支付id
    */
   private String prepayId;

   /**
    * 微信支付签名
    */
   private String paySign;

   /**
    *  结果
    *  0代表统一下单成功
    *  1代表下单失败
    */
   private Integer status;

   /**
    *  支付失败的原因
    */
   private String returnMsg;

}
