package com.dkm.pay.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/3/17
 * @vesion 1.0
 **/
@Data
public class PayVo {

   /**
    * 订单号
    */
   private String orderNo;

   /**
    * 第三方流水号
    */
   private String tradeNo;

   /**
    * 支付成功--返回SUCCESS
    * 支付失败--返回FAIL
    */
   private String msg;
}
