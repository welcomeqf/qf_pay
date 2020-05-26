package com.dkm.pay.entity.wxBo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/3/23
 * @vesion 1.0
 **/
@Data
public class WxInsertBo {

   /**
    * 订单号
    */
   private String orderNo;

   /**
    * 价格
    */
   private Double price;

   /**
    * 商品描述
    */
   private String body;

   /**
    * 异步回调
    */
   private String notifyUrl;
}
