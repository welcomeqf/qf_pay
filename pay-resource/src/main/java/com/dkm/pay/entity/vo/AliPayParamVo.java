package com.dkm.pay.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/3/18
 * @vesion 1.0
 **/
@Data
public class AliPayParamVo {

   private String trade_no;

   private String out_trade_no;

   private String trade_status;

   private LocalDateTime notify_time;

   private String notify_type;

   private String notify_id;

   private String charset;

   private String version;

   private String sign_type;

   private String sign;

   private String auth_app_id;

   private String app_id;

   private String out_biz_no;

   private String buyer_id;

   private String seller_id;

   private Double total_amount;

   private Double receipt_amount;

   private Double invoice_amount;

   private Double buyer_pay_amount;

   private Double point_amount;

   private Double refund_fee;

   private String subject;

   private String body;

   private LocalDateTime gmt_create;

   private LocalDateTime gmt_payment;

   private LocalDateTime gmt_refund;

   private LocalDateTime gmt_close;

   private String fund_bill_list;

   private String voucher_detail_list;

   private String passback_params;
}
