package com.dkm.wxpay.entity;

import lombok.Data;

/**
 * @author qf
 * @date 2020/7/21
 * @vesion 1.0
 **/
@Data
public class WxRefundVo {

   /**
    * 0--退款成功
    * 1--退款失败
    */
   private Integer status;
}
