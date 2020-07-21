package com.dkm.wxpay.entity;

import lombok.Data;

/**
 * 微信退款结果返回
 * 具体字段看微信支付官方文档
 * @author qf
 * @date 2020/7/21
 * @vesion 1.0
 **/
@Data
public class WxRefundResultVo {

   private String total_fee;

   private String refund_channel_$n;

   private String refund_status_$n;

   private String refund_success_time_$n;

   /**
    *  退款查询结果
    *  0--查询成功
    *  1--查询失败
    */
   private Integer status;
}
