package com.dkm.alipay.service;


import com.dkm.alipay.entity.AliRefundVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qf
 * @date 2020/7/22
 * @vesion 1.0
 **/
public interface IAliPayService {

   /**
    *  支付宝支付
    * @param orderNo 订单号
    * @param price 金额
    * @param subject 订单标题
    * @param body 订单描述
    * @param returnUrl 前端跳转页面
    * @param notifyUrl 后端回调接口
    * @param httpResponse 响应
    */
   void aliPcPay (String orderNo, Double price, String subject, String body, String returnUrl, String notifyUrl, HttpServletResponse httpResponse);

   /**
    *  支付宝支付(退款)查询
    * @param orderNo 订单号
    * @param queryType 查询类型 0-支付状况 1-退款状态
    * @return 返回查询结果
    */
   Object queryPay (String orderNo, Integer queryType);

   /**
    *  支付宝退款
    * @param orderNo 订单号
    * @param money 金额
    * @return 返回退款结果
    */
   AliRefundVo aliPayCreateOrderRefund (String orderNo, Double money);

   /**
    *  支付宝回调接口
    * @param request 请求参数
    * @return 返回结果
    */
   String getReturnInfo (HttpServletRequest request);
}
