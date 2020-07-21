package com.dkm.wxpay.entity;

import lombok.Data;

/**
 * 微信支付状况查询结果返回
 * 具体字段意思看官网
 * @author qf
 * @date 2020/7/21
 * @vesion 1.0
 **/
@Data
public class WxAppResultVo {

   private String trade_type;

   private String trade_state;

   private String bank_type;

   private String total_fee;

   private String time_end;

   private String trade_state_desc;

   /**
    * 退款查询结果
    * 0--查询成功
    * 1--查询失败
    */
   private Integer status;
}
