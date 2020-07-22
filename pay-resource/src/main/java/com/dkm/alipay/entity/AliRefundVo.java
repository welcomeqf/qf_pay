package com.dkm.alipay.entity;

import lombok.Data;

/**
 * @author qf
 * @date 2020/7/22
 * @vesion 1.0
 **/
@Data
public class AliRefundVo {

   /**
    *  退款结果
    *  0--退款成功
    *  1--退款失败
    */
   private Integer status;
}
