package com.dkm.pay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/3/17
 * @vesion 1.0
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("pay_info")
public class PayInfo extends Model<PayInfo> {

   private Long id;

   /**
    * 设备ID
    */
   private Long authId;

   /**
    * 订单号
    */
   private String orderNo;

   /**
    * 支付单号
    */
   private String payNo;

   /**
    * 支付类别
    * 0--支付宝
    * 1--微信
    * 2--银联
    */
   private Integer payType;

   /**
    * 支付订单金额
    */
   private Double payMoney;

   /**
    * 支付时间
    */
   private LocalDateTime payTime;

   /**
    * 是否成功
    * 0--成功
    * 1--失败
    */
   private Integer isPaySuccess;

   /**
    * 创建时间
    */
   private LocalDateTime createDate;

   /**
    * 修改时间
    */
   private LocalDateTime updateDate;

   /**
    * 订单标题
    */
   private String paySubject;

   /**
    * 订单描述
    */
   private String payBody;

   /**
    * 异步回调接口通知
    */
   private String returnUrl;
}
