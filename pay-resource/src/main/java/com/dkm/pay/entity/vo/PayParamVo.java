package com.dkm.pay.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/3/17
 * @vesion 1.0
 **/
@Data
public class PayParamVo {

   private String orderNo;

   /**
    * **
    *     * 是否成功
    *     * 0--成功
    *     * 1--失败
    */
   private Integer isPaySuccess;

   /**
    * 第三方支付流水号
    */
   private String payNo;

   /**
    * 0--支付宝
    * 1--微信
    * 2--银联
    *
    */
   private Integer payType;
}
