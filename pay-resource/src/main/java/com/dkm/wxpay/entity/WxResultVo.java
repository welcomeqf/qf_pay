package com.dkm.wxpay.entity;

import lombok.Data;

/**
 * @author qf
 * @date 2020/7/21
 * @vesion 1.0
 **/
@Data
public class WxResultVo {

   /**
    * 订单号
    */
   private String orderNo;

   /**
    * 微信付款单号
    */
   private String paymentNo;

   /**
    * 付款时间
    */
   private String paymentTime;

   /**
    * 付款状态
    * 0--成功
    * 1--失败
    */
   private Integer status;

   /**
    * 付款失败原因
    */
   private String returnMsg;
}
