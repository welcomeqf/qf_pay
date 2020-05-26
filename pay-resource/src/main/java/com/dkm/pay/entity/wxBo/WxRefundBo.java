package com.dkm.pay.entity.wxBo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/3/23
 * @vesion 1.0
 **/
@Data
public class WxRefundBo {

   /**
    * 微信支付的商户ID
    */
   private String mchId;

   /**
    * 微信的AppID
    */
   private String wxAppId;

   /**
    * 私钥
    */
   private String paterNerKey;
}
