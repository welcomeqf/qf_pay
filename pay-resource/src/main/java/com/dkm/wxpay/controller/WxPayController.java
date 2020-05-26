package com.dkm.wxpay.controller;

import com.alibaba.fastjson.JSON;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.httpclient.HttpClientUtils;
import com.dkm.httpclient.HttpResult;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.pay.entity.vo.PayParamVo;
import com.dkm.pay.entity.vo.PayVo;
import com.dkm.pay.entity.wxBo.WxInsertBo;
import com.dkm.pay.entity.wxBo.WxRefundBo;
import com.dkm.pay.entity.wxBo.WxResultBo;
import com.dkm.pay.service.IPayInfoService;
import com.dkm.utils.StringUtils;
import com.dkm.wxpay.utils.HttpRequest;
import com.dkm.wxpay.utils.WxUtils;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author qf
 * @date 2020/3/19
 * @vesion 1.0
 **/
@Api
@Slf4j
@RestController
@RequestMapping("/v1/wx")
public class WxPayController {


   @Autowired
   private IPayInfoService payInfoService;

   @Autowired
   private HttpClientUtils apiService;

   @Value("${wx.queryUrl}")
   private String queryUrl;

   @Value("{wx.queryRefundUrl}")
   private String queryRefundUrl;

   @Value("${wx.url}")
   private String wxUrl;

   @Value("${wx.notifyUrl}")
   private String notify_url;

   @Value("${wx.refundUrl}")
   private String refundUrl;

   @Value("${wx.ip}")
   private String ip;


   @ApiOperation(value = "微信App支付", notes = "微信App支付")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "price", value = "支付金钱", required = true, dataType = "Double", paramType = "path"),
         @ApiImplicitParam(name = "body", value = "订单描述", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "notifyUrl", value = "回调接口", required = true, dataType = "String", paramType = "path"),
   })
   @GetMapping("/appPay")
   @CheckToken
   @CrossOrigin
   public Map<String, String> orders(@RequestParam("body") String body,
                                     @RequestParam("orderNo") String orderNo,
                                     @RequestParam("price") Double price,
                                     @RequestParam("notifyUrl") String notifyUrl) {

      //先执行业务操作，再去调用微信支付
      WxInsertBo bo = new WxInsertBo();
      bo.setOrderNo(orderNo);
      bo.setBody(body);
      bo.setNotifyUrl(notifyUrl);
      bo.setPrice(price);
      WxResultBo wxResultBo = payInfoService.insertWxPayInfo(bo);

      try {

         // 拼接统一下单地址参数
         Map<String, String> paraMap = new HashMap<String, String>();
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
         paraMap.put("notify_url", notify_url);
         // 将所有参数(map)转xml格式
         String xml = WXPayUtil.generateSignedXml(paraMap, wxResultBo.getPaterNerKey());

         // 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder

         String xmlStr = HttpRequest.httpsRequest(wxUrl, "POST", xml);

         // 以下内容是返回前端页面的json数据
         // 预支付id
         String prepay_id = "";
         if (xmlStr.indexOf("SUCCESS") != -1) {
            Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
            prepay_id = map.get("prepay_id");
         }

         Map<String, String> payMap = new HashMap<String, String>();
         payMap.put("appId", wxResultBo.getWxAppId());
         payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
         payMap.put("nonce_str", WXPayUtil.generateNonceStr());
         payMap.put("signType", "MD5");
         payMap.put("prepayId", prepay_id);
         Map<String, String> resultSign = WXPayUtil.xmlToMap(xml);

         paraMap.put("paySign",resultSign.get("sign"));
         //将这个6个参数传给前端
         return payMap;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }


   @ApiOperation(value = "微信支付(退款)查询接口", notes = "微信支付(退款)查询接口")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "queryType", value = "(查询类型 0-支付状况 1-退款状态)", required = true, dataType = "int", paramType = "path"),
   })
   @GetMapping("/queryPay")
   @CheckToken
   @CrossOrigin
   public Map<String,String> queryPay (@RequestParam("orderNo") String orderNo,
                           @RequestParam("queryType") Integer queryType) {

      WxRefundBo refundBo = payInfoService.queryWxAppId();

      Map<String, String> payMap = new HashMap<String, String>();
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
         Map<String,String> result = new HashMap<>();

         if ("SUCCESS".equals(map.get("return_code")) || "SUCCESS".equals(map.get("result_code"))) {
            //查询成功  返回数据
            result.put("trade_type",map.get("trade_type"));
            result.put("trade_state",map.get("trade_state"));
            result.put("bank_type",map.get("bank_type"));
            String totalFee = map.get("total_fee");
            Integer value = Integer.valueOf(totalFee);
            result.put("total_fee",String.valueOf(BigDecimal.valueOf(Long.valueOf(value)).divide(new BigDecimal(100))));
            result.put("time_end",map.get("time_end"));
            result.put("trade_state_desc",map.get("trade_state_desc"));
            result.put("status","SUCCESS");
         } else {
            result.put("status","FAIL");
         }

         return result;
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

      Map<String,String> resultMap = new HashMap<>();

      if ("SUCCESS".equals(refundMap.get("return_code")) || "SUCCESS".equals(refundMap.get("result_code"))) {
         //退款成功
         //返回数据
         String totalFee = refundMap.get("total_fee");
         Integer value = Integer.valueOf(totalFee);
         resultMap.put("total_fee",String.valueOf(BigDecimal.valueOf(Long.valueOf(value)).divide(new BigDecimal(100))));
         resultMap.put("refund_channel_$n",refundMap.get("refund_channel_$n"));
         resultMap.put("refund_status_$n",refundMap.get("refund_status_$n"));
         resultMap.put("refund_success_time_$n",refundMap.get("refund_success_time_$n"));
         resultMap.put("status","SUCCESS");
      } else {
         //查询失败
         resultMap.put("status","FAIL");
      }

      return resultMap;
   }


   @ApiOperation(value = "微信退款接口", notes = "微信退款接口")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "orderPrice", value = "订单金额", required = true, dataType = "double", paramType = "path"),
         @ApiImplicitParam(name = "refundPrice", value = "退款金额", required = true, dataType = "double", paramType = "path"),
   })
   @GetMapping("/refundWxPay")
   @CheckToken
   @CrossOrigin
   public Map<String,Integer> refundWxPay (@RequestParam("orderNo") String orderNo,
                            @RequestParam("refundPrice") Double refundPrice,
                            @RequestParam("orderPrice") Double orderPrice) {

      WxRefundBo refundBo = payInfoService.queryWxAppId();

      Map<String, String> payMap = new HashMap<String, String>();
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

      Map<String,Integer> result = new HashMap<>(1);
      if ("SUCCESS".equals(map.get("return_code"))) {
         //退款成功
         result.put("status",0);
      } else {
         result.put("status",1);
      }

      return result;
   }

   /**
    * 微信回调接口
    * @param request
    * @param response
    * @return
    */
   @RequestMapping("/notify")
   @CrossOrigin
   public String callBack(HttpServletRequest request, HttpServletResponse response) {
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

         if (notifyMap.get("result_code").equals("SUCCESS")) {

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

      return null;
   }



   @ApiOperation(value = "微信公众号支付", notes = "微信公众号支付")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "price", value = "支付金钱", required = true, dataType = "Double", paramType = "path"),
         @ApiImplicitParam(name = "openId", value = "openId", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "body", value = "订单描述", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "notifyUrl", value = "回调接口", required = true, dataType = "String", paramType = "path"),
   })
   @GetMapping("/jsApiPay")
   @CrossOrigin
   @CheckToken
   public Map<String,String> pay (@RequestParam("body") String body,
                                  @RequestParam("orderNo") String orderNo,
                                  @RequestParam("price") Double price,
                                  @RequestParam("notifyUrl") String notifyUrl,
                                  @RequestParam("openId") String openId) {

      //先执行业务操作，再去调用微信支付
      WxInsertBo bo = new WxInsertBo();
      bo.setOrderNo(orderNo);
      bo.setBody(body);
      bo.setNotifyUrl(notifyUrl);
      bo.setPrice(price);
      WxResultBo wxResultBo = payInfoService.insertWxPayInfo(bo);

      // 拼接统一下单地址参数
      Map<String, String> map = new HashMap<>();
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
         map.put("notify_url", notify_url);
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

         if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code")) ) {
            //统一下单成功后保存商户后台系统的订单表

            //处理返回APP端的参数及生成sign
            resultMap.put("appId", result.get("appid"));
            resultMap.put("mchId", result.get("mch_id"));
            resultMap.put("nonceStr",result.get("nonce_str"));
            resultMap.put("sign",result.get("sign"));
            resultMap.put("signType", "MD5");
            resultMap.put("tradeType",result.get("trade_type"));
            resultMap.put("prepayId",result.get("prepay_id"));
            resultMap.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
            //根据算法生成签名
            Map<String, String> resultSign = WXPayUtil.xmlToMap(xml);

            resultMap.put("paySign",resultSign.get("sign"));


            //status 0代表统一下单成功  1代表下单失败
            resultMap.put("status","0");
            return resultMap;
         } else {
            resultMap.put("return_msg",result.get("return_msg"));
            resultMap.put("status","1");
         }
         return resultMap;

      } catch (Exception e) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"微信公众号支付统一下单失败");
      }

   }


}
