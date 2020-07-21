package com.dkm.wxpay.service;

import com.dkm.wxpay.entity.WxJsApiPayResultVo;
import com.dkm.wxpay.entity.WxPayResultVo;
import com.dkm.wxpay.entity.WxRefundVo;
import com.dkm.wxpay.entity.WxResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qf
 * @date 2020/7/21
 * @vesion 1.0
 **/
public interface IWxService {

   /**
    *  企业向个人付款
    *  微信零钱到账
    * @param openId 微信个人的openId
    * @param price 提现金额
    * @return 返回结果给前端
    */
   WxResultVo companyToPerson(String openId, Double price);

   /**
    *  微信APP支付
    * @param body 支付内容
    * @param orderNo 订单号
    * @param price 支付金额
    * @param notifyUrl 异步回调接口
    * @return 返回支付信息给前端
    */
   WxPayResultVo wxAppPay (String body, String orderNo, Double price, String notifyUrl);

   /**
    *  微信支付结果或退款结果查询
    * @param orderNo 订单号
    * @param queryType 0--支付结果查询  1--退款结果查询
    * @return 返回查询结果
    */
   Object wxPayRefundResultQuery (String orderNo, Integer queryType);

   /**
    *  微信回调接口
    * @param request 请求
    * @param response 响应
    */
   void callBack (HttpServletRequest request, HttpServletResponse response);

   /**
    *  微信jsApi支付
    * @param body 内容描述
    * @param orderNo 订单号
    * @param price 金额
    * @param notifyUrl 异步回调
    * @param openId 微信openId
    * @return 返回支付结果
    */
   WxJsApiPayResultVo wxJsApiPay (String body, String orderNo, Double price, String notifyUrl, String openId);

   /**
    *  微信退款
    * @param orderNo 订单号
    * @param refundPrice 退款金额
    * @param orderPrice 订单金额
    * @return 返回退款结果
    */
   WxRefundVo refundWxPay (String orderNo, Double refundPrice, Double orderPrice);
}
