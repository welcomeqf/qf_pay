package com.dkm.pay.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/3/17
 * @vesion 1.0
 **/
@Data
public class PayReturnVo {

   /**
    * 订单号
    */
   private String orderNo;

   /**
    * 金额
    */
   private Double price;

   /**
    * 订单标题
    */
   private String subject;

   /**
    * 订单描述
    */
   private String body;

   /**
    * 异步回调接口
    */
   private String returnUrl;
}
