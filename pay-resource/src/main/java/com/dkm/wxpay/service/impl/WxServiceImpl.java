package com.dkm.wxpay.service.impl;

import com.alibaba.fastjson.JSON;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.httpclient.HttpClientUtils;
import com.dkm.httpclient.HttpResult;
import com.dkm.pay.entity.vo.PayParamVo;
import com.dkm.pay.entity.vo.PayVo;
import com.dkm.pay.entity.wxBo.WxInsertBo;
import com.dkm.pay.entity.wxBo.WxRefundBo;
import com.dkm.pay.entity.wxBo.WxResultBo;
import com.dkm.pay.service.IPayInfoService;
import com.dkm.utils.IdGenerator;
import com.dkm.wxpay.entity.*;
import com.dkm.wxpay.service.IWxService;
import com.dkm.wxpay.utils.HttpRequest;
import com.dkm.wxpay.utils.WxUtils;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qf
 * @date 2020/7/21
 * @vesion 1.0
 **/
@Service
@Slf4j
public class WxServiceImpl implements IWxService {


   @Value("${wx.pay}")
   private String wxUrl;

   /**
    * 证书位置
    */
   @Value("${wx.certUrl}")
   private String certUrl;

   @Autowired
   private IdGenerator idGenerator;

   @Autowired
   private IPayInfoService payInfoService;

   @Autowired
   private HttpClientUtils apiService;

   @Value("${wx.queryUrl}")
   private String queryUrl;

   @Value("{wx.queryRefundUrl}")
   private String queryRefundUrl;

   @Value("${wx.notifyUrl}")
   private String notifyWxUrl;

   @Value("${wx.refundUrl}")
   private String refundUrl;

   @Value("${wx.ip}")
   private String ip;

   private String scc = "SUCCESS";

   private String returnCode = "return_code";

   private String resultCode = "result_code";

   @Override
   public WxResultVo companyToPerson(String openId, Double price) {
      WxResultVo resultVo = new WxResultVo();

      //先执行业务操作，再去调用微信支付
      String orderCode = idGenerator.getOrderCode();
      WxInsertBo bo = new WxInsertBo();
      bo.setOrderNo(orderCode);
      bo.setBody(null);
      bo.setNotifyUrl(null);
      bo.setPrice(price);
      WxResultBo wxResultBo = payInfoService.insertWxPayInfo(bo);

      try {
         // 拼接统一下单地址参数
         Map<String, String> paraMap = new HashMap<>(8);

         paraMap.put("mch_appid", wxResultBo.getWxAppId());
         // 商户ID
         paraMap.put("mchid", wxResultBo.getMchId());
         paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
         paraMap.put("partner_trade_no", orderCode);
         paraMap.put("openid", openId);
         // 支付金额，单位分
         //元转分
         Integer money = WxUtils.changeY2F(price);
         paraMap.put("amount", String.valueOf(money));
         paraMap.put("check_name", "NO_CHECK");
         paraMap.put("desc", "红包");
         // 将所有参数(map)转xml格式
         String xml = WXPayUtil.generateSignedXml(paraMap, wxResultBo.getPaterNerKey());

         String xmlStr = WxUtils.doPostSSL(wxUrl, xml, wxResultBo.getMchId(), certUrl);

         //将返回数据XML转为Map格式
         Map<String, String> result = WXPayUtil.xmlToMap(xmlStr);

         if (scc.equals(result.get(returnCode)) && scc.equals(result.get(resultCode)) ) {
            resultVo.setOrderNo(result.get("partner_trade_no"));
            resultVo.setPaymentNo(result.get("payment_no"));
            resultVo.setPaymentTime(result.get("payment_time"));
            resultVo.setStatus(0);
            return resultVo;
         } else {
            resultVo.setStatus(1);
            resultVo.setReturnMsg(result.get("return_msg"));
            return resultVo;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   @Override
   public WxPayResultVo wxAppPay(String body, String orderNo, Double price, String notifyUrl) {
      //先执行业务操作，再去调用微信支付
      WxInsertBo bo = new WxInsertBo();
      bo.setOrderNo(orderNo);
      bo.setBody(body);
      bo.setNotifyUrl(notifyUrl);
      bo.setPrice(price);
      WxResultBo wxResultBo = payInfoService.insertWxPayInfo(bo);

      try {
         // 拼接统一下单地址参数
         Map<String, String> paraMap = new HashMap<>(16);

         // 获取请求ip地址
         paraMap.put("appid", wxResultBo.getWxAppId());
         // 商户ID
         paraMap.put("mch_id", wxResultBo.getMchId());
         paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
         paraMap.put("body", body);
         paraMap.put("out_trade_no", orderNo);
         paraMap.put("spbill_create_ip", ip);
         // 支付金额，单位分
         //元转分
         Integer money = WxUtils.changeY2F(price);
         paraMap.put("total_fee", String.valueOf(money));
         paraMap.put("trade_type", "APP");
         paraMap.put("notify_url", notifyWxUrl);
         // 将所有参数(map)转xml格式
         String xml = WXPayUtil.generateSignedXml(paraMap, wxResultBo.getPaterNerKey());

         // 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
         String xmlStr = HttpRequest.httpsRequest(wxUrl, "POST", xml);

         // 以下内容是返回前端页面的json数据
         // 预支付id
         String prepay_id = "";
         if (xmlStr.contains(scc)) {
            Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
            prepay_id = map.get("prepay_id");
         }

         Map<String, String> resultSign = WXPayUtil.xmlToMap(xml);

         WxPayResultVo resultVo = new WxPayResultVo();
         resultVo.setAppId(wxResultBo.getWxAppId());
         resultVo.setNonceStr(WXPayUtil.generateNonceStr());
         resultVo.setPaySign(resultSign.get("sign"));
         resultVo.setPrepayId(prepay_id);
         resultVo.setSignType("MD5");
         resultVo.setTimeStamp(WXPayUtil.getCurrentTimestamp() + "");
         //将这个6个参数传给前端
         return resultVo;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   @Override
   public Object wxPayRefundResultQuery(String orderNo, Integer queryType) {

      //查询一些微信支付的信息
      WxRefundBo refundBo = payInfoService.queryWxAppId();

      Map<String, String> payMap = new HashMap<>(4);
      payMap.put("appid", refundBo.getWxAppId());
      payMap.put("mch_id", refundBo.getMchId());
      payMap.put("nonce_str", WXPayUtil.generateNonceStr());
      payMap.put("out_trade_no",orderNo);

      if (queryType == 0) {
         //查询支付状况
         // 将所有参数(map)转xml格式
         String xml = null;
         try {
            xml = WXPayUtil.generateSignedXml(payMap, refundBo.getPaterNerKey());
         } catch (Exception e) {
            e.printStackTrace();
         }

         //调查询订单接口
         String xmlStr = HttpRequest.httpsRequest(queryUrl, "POST", xml);

         Map<String, String> map = new HashMap<>();
         try {
            map = WXPayUtil.xmlToMap(xmlStr);
         } catch (Exception e) {
            e.printStackTrace();
         }

         WxAppResultVo wxAppResultVo = new WxAppResultVo();
         if (scc.equals(map.get(returnCode)) || scc.equals(map.get(resultCode))) {
            //查询成功  返回数据
            wxAppResultVo.setTrade_state(map.get("trade_state"));
            wxAppResultVo.setTrade_type(map.get("trade_type"));
            wxAppResultVo.setBank_type(map.get("bank_type"));
            wxAppResultVo.setTime_end(map.get("time_end"));
            wxAppResultVo.setTrade_state_desc(map.get("trade_state_desc"));
            String totalFee = map.get("total_fee");
            Integer value = Integer.valueOf(totalFee);
            wxAppResultVo.setStatus(0);
            wxAppResultVo.setTotal_fee(String.valueOf(BigDecimal.valueOf(Long.valueOf(value)).divide(new BigDecimal(100))));
         } else {
            wxAppResultVo.setStatus(1);
         }

         return wxAppResultVo;
      }

      //查询退款状况
      // 将所有参数(map)转xml格式
      String refundXml = null;
      try {
         refundXml = WXPayUtil.generateSignedXml(payMap, refundBo.getPaterNerKey());
      } catch (Exception e) {
         e.printStackTrace();
      }

      //调查询订单接口
      String refundResult = HttpRequest.httpsRequest(queryRefundUrl, "POST", refundXml);

      Map<String, String> refundMap = new HashMap<>();
      try {
         refundMap = WXPayUtil.xmlToMap(refundResult);
      } catch (Exception e) {
         e.printStackTrace();
      }

      WxRefundResultVo wxRefundResultVo = new WxRefundResultVo();
      if (scc.equals(refundMap.get(returnCode)) || scc.equals(refundMap.get(resultCode))) {
         //退款成功 返回数据
         String totalFee = refundMap.get("total_fee");
         int value = Integer.parseInt(totalFee);
         wxRefundResultVo.setTotal_fee(String.valueOf(BigDecimal.valueOf(Long.valueOf(value)).divide(new BigDecimal(100))));
         wxRefundResultVo.setRefund_channel_$n(refundMap.get("refund_channel_$n"));
         wxRefundResultVo.setRefund_status_$n(refundMap.get("refund_status_$n"));
         wxRefundResultVo.setRefund_success_time_$n(refundMap.get("refund_success_time_$n"));
         wxRefundResultVo.setStatus(0);
      } else {
         //查询失败
         wxRefundResultVo.setStatus(1);
      }

      return wxRefundResultVo;
   }

   @Override
   public void callBack(HttpServletRequest request, HttpServletResponse response) {
      InputStream is = null;
      try {
         // 获取请求的流信息(这里是微信发的xml格式所有只能使用流来读)
         is = request.getInputStream();
         String str = WxUtils.getStreamToStr(is);
         /// 将微信发的xml转map
         Map<String, String> notifyMap = WXPayUtil.xmlToMap(str);

         PayVo payVo = new PayVo();
         // 商户订单号
         String orderNo = notifyMap.get("out_trade_no");
         //微信支付单号
         String payNo = notifyMap.get("transaction_id");

         //修改数据库微信支付状态·
         PayParamVo vo = new PayParamVo();
         vo.setOrderNo(orderNo);
         vo.setPayNo(payNo);
         vo.setPayType(1);

         if (notifyMap.get(resultCode).equals(scc)) {
            vo.setIsPaySuccess(0);
            //设置成功
            payVo.setMsg("SUCCESS");
         } else {
            vo.setIsPaySuccess(1);
            payVo.setMsg("FAIL");
         }
         //得到回调接口
         String returnUrl = payInfoService.updateAliPayStatus(vo);

         //本地主动去回调接口
         payVo.setOrderNo(orderNo);
         payVo.setTradeNo(payNo);

         String s = JSON.toJSONString(payVo);

         //回调接口
         HttpResult httpResult = apiService.doPost(returnUrl, s);
         log.info("---异步调用应用返回支付结果:" + httpResult);

         // 告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
         response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }

   @Override
   public WxJsApiPayResultVo wxJsApiPay(String body, String orderNo, Double price, String notifyUrl, String openId) {
      //先执行业务操作，再去调用微信支付
      WxInsertBo bo = new WxInsertBo();
      bo.setOrderNo(orderNo);
      bo.setBody(body);
      bo.setNotifyUrl(notifyUrl);
      bo.setPrice(price);
      WxResultBo wxResultBo = payInfoService.insertWxPayInfo(bo);

      // 拼接统一下单地址参数
      Map<String, String> map = new HashMap<>(16);
      // 获取请求ip地址
      try {
         String nonceStr = WXPayUtil.generateNonceStr();
         map.put("appid", wxResultBo.getWxAppId());
         map.put("mch_id", wxResultBo.getMchId());
         map.put("nonce_str", nonceStr);
         map.put("body", body);
         map.put("out_trade_no", orderNo);
         //处理金额去除小数点
         //元转分
         Integer money = WxUtils.changeY2F(price);
         map.put("total_fee", String.valueOf(money));
         map.put("spbill_create_ip", ip);
         map.put("notify_url", notifyWxUrl);
         map.put("trade_type", "JSAPI");
         map.put("openid", openId);

         //根据算法生成签名
         String xml = WXPayUtil.generateSignedXml(map, wxResultBo.getPaterNerKey());

         //通过HTTPS请求调用微信的统一下单请求
         String xmlStr = HttpRequest.httpsRequest(wxUrl, "POST", xml);

         //检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改.

         //将返回数据XML转为Map格式
         Map<String, String> result = WXPayUtil.xmlToMap(xmlStr);

         Map<String,String> resultMap = new HashMap<>();

         WxJsApiPayResultVo vo = new WxJsApiPayResultVo();

         if (scc.equals(result.get(returnCode)) && scc.equals(result.get(resultCode)) ) {
            //统一下单成功后保存商户后台系统的订单表
            //处理返回APP端的参数及生成sign
            vo.setAppId(result.get("appid"));
            vo.setMchId(result.get("mch_id"));
            vo.setNonceStr(result.get("nonce_str"));
            vo.setSign(result.get("sign"));
            vo.setSignType("MD5");
            vo.setTradeType(result.get("trade_type"));
            vo.setPrepayId(result.get("prepay_id"));
            vo.setTimeStamp(WXPayUtil.getCurrentTimestamp() + "");
            //根据算法生成签名
            Map<String, String> resultSign = WXPayUtil.xmlToMap(xml);

            vo.setPaySign(resultSign.get("sign"));
            //status 0代表统一下单成功  1代表下单失败
            vo.setStatus(0);

            return vo;
         } else {
            vo.setReturnMsg(result.get("return_msg"));
            vo.setStatus(1);
         }
         return vo;

      } catch (Exception e) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"微信公众号支付统一下单失败");
      }
   }


   @Override
   public WxRefundVo refundWxPay(String orderNo, Double refundPrice, Double orderPrice) {
      WxRefundBo refundBo = payInfoService.queryWxAppId();

      Map<String, String> payMap = new HashMap<>(8);
      payMap.put("appid", refundBo.getWxAppId());
      payMap.put("mch_id", refundBo.getMchId());
      payMap.put("nonce_str", WXPayUtil.generateNonceStr());
      payMap.put("out_trade_no",orderNo);
      payMap.put("out_refund_no", orderNo);
      Integer refundMoney = WxUtils.changeY2F(refundPrice);
      Integer orderMoney = WxUtils.changeY2F(orderPrice);
      payMap.put("total_fee", String.valueOf(orderMoney));
      payMap.put("refund_fee",String.valueOf(refundMoney));

      // 将所有参数(map)转xml格式
      String xml = null;
      try {
         xml = WXPayUtil.generateSignedXml(payMap, refundBo.getPaterNerKey());
      } catch (Exception e) {
         e.printStackTrace();
      }

      //调退款接口
      String xmlStr = HttpRequest.httpsRequest(refundUrl, "POST", xml);

      Map<String, String> map = new HashMap<>();
      try {
         map = WXPayUtil.xmlToMap(xmlStr);
      } catch (Exception e) {
         e.printStackTrace();
      }
      WxRefundVo vo = new WxRefundVo();
      if (scc.equals(map.get(returnCode))) {
         //退款成功
         vo.setStatus(0);
      } else {
         vo.setStatus(1);
      }

      return vo;
   }


}
