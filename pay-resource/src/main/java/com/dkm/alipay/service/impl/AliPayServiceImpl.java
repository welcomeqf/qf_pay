package com.dkm.alipay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.dkm.alipay.entity.AliRefundVo;
import com.dkm.alipay.service.IAliPayService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.httpclient.HttpClientUtils;
import com.dkm.httpclient.HttpResult;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.pay.entity.PayInfo;
import com.dkm.pay.entity.vo.PayParamVo;
import com.dkm.pay.entity.vo.PayReturnVo;
import com.dkm.pay.entity.vo.PayVo;
import com.dkm.pay.service.IPayInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qf
 * @date 2020/7/22
 * @vesion 1.0
 **/
@Slf4j
@Service
public class AliPayServiceImpl implements IAliPayService {


   @Autowired
   private IPayInfoService payInfoService;

   @Autowired
   private HttpClientUtils apiService;

   @Value("${pay.testUrl}")
   private String url;

   @Value("${pay.aliPrivateKey}")
   private String privateKey;

   @Value("${pay.aliPublicKey}")
   private String aliPublicKey;

   @Value("${pay.notifyServerUrl}")
   private String notifyServerUrl;

   private String tradeSuccess = "TRADE_SUCCESS";

   @Autowired
   private LocalUser localUser;

   @Override
   public void aliPcPay(String orderNo, Double price, String subject, String body, String returnUrl, String notifyUrl, HttpServletResponse httpResponse) {
      //先执行业务操作，再去调用阿里的支付
      PayReturnVo vo = new PayReturnVo();
      vo.setOrderNo(orderNo);
      vo.setBody(body);
      vo.setPrice(price);
      vo.setReturnUrl(notifyUrl);
      vo.setSubject(subject);
      String appId = payInfoService.insertPayInfo(vo);

      /**
       * 获得初始化的alipayClient
       */
      AlipayClient alipayClient = new DefaultAlipayClient(
            url,
            appId,
            privateKey,
            "json",
            "UTF-8",
            aliPublicKey,
            "RSA2");
      //创建API对应的request
      AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
      alipayRequest.setReturnUrl(returnUrl);
      //在公共参数中设置回跳和通知地址

      alipayRequest.setNotifyUrl(notifyServerUrl);
      alipayRequest.setBizContent("{" +
            //订单号
            "    \"out_trade_no\":\" " + orderNo + "\"," +
            "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
            "    \"total_amount\":" + price + "," +
            //订单标题
            "    \"subject\":\" + " + subject + "\"," +
            "    \"body\":\" + " + body + "\"," +
            //该参数作为回传参数，传给支付宝什么  支付宝回原封不动的返回   需要编码
            //本参数必须进行UrlEncode之后才可以发送给支付宝。
            "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
            //业务扩展参数
            "    \"extend_params\":{" +
            "    \"sys_service_provider_id\":\"2088511833207846\"" +
            "    }"+
            "  }");//填充业务参数
      String form="";
      try {
         //调用SDK生成表单
         form = alipayClient.pageExecute(alipayRequest).getBody();
      } catch (AlipayApiException e) {
         e.printStackTrace();
      }
      httpResponse.setContentType("text/html;charset=utf-8");
      //直接将完整的表单html输出到页面
      try {
         httpResponse.getWriter().write(form);
         httpResponse.getWriter().flush();
         httpResponse.getWriter().close();
      } catch (Exception e) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "输出表单出错.");
      }
   }

   @Override
   public Object queryPay(String orderNo, Integer queryType) {
      UserLoginQuery user = localUser.getUser();
      //查询appId
      String appId = payInfoService.queryAppId(user.getId());

      PayInfo payInfo = payInfoService.queryOne(orderNo);

      if (queryType == 0) {
         //查询支付状况
         AlipayClient alipayClient = new DefaultAlipayClient(
               url,
               appId,
               privateKey,
               "json",
               "UTF-8",
               aliPublicKey,
               "RSA2");
         AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
         request.setBizContent("{" +
               "\"out_trade_no\":\"" + orderNo + "\"," +
               "\"trade_no\":\"" + payInfo.getPayNo() +"\"," +
//               "\"org_pid\":\"\"," +
               "      \"query_options\":[" +
               "        \"TRADE_SETTLE_INFO\"" +
               "      ]" +
               "  }");
         AlipayTradeQueryResponse response = null;
         try {
            response = alipayClient.execute(request);
         } catch (AlipayApiException e) {
            e.printStackTrace();
         }
         return response;
      }

      //查询退款状态
      AlipayClient alipayClient = new DefaultAlipayClient(
            url,
            appId,
            privateKey,
            "json",
            "UTF-8",
            aliPublicKey,
            "RSA2");
      AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
      request.setBizContent("{" +
            "\"out_trade_no\":\"" + orderNo + "\"," +
            "\"trade_no\":\"" + payInfo.getPayNo() +"\"," +
            "\"out_request_no\":\"" + orderNo + "\"," +
            "\"org_pid\":\"2088101117952222\"" +
            "  }");
      AlipayTradeFastpayRefundQueryResponse response = null;
      try {
         response = alipayClient.execute(request);
      } catch (AlipayApiException e) {
         e.printStackTrace();
      }

      return response;
   }

   @Override
   public AliRefundVo aliPayCreateOrderRefund(String orderNo, Double money) {

      UserLoginQuery user = localUser.getUser();

      String appId = payInfoService.queryAppId(user.getId());

      PayInfo payInfo = payInfoService.queryOne(orderNo);

      AlipayClient alipayClient = new DefaultAlipayClient(url,
            appId, privateKey,
            "json", "UTF-8", aliPublicKey, "RSA2");
      AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

      AlipayTradeRefundModel model= new AlipayTradeRefundModel();
      //订单支付时传入的商户订单号
      model.setOutTradeNo(orderNo);
      //支付宝交易号
      model.setTradeNo(payInfo.getPayNo());
      model.setOutRequestNo(orderNo);
      //refund_amount    需要退款的金额，该金额不能大于订单金额,单位为元
      model.setRefundAmount(String.valueOf(money));

      //请求参数
      request.setBizModel(model);

      AlipayTradeRefundResponse response=null;
      try {
         response = alipayClient.execute(request);
      }catch ( AlipayApiException e){
         String massage = "aliPay.trade.refund退款接口：订单签名错误";
         log.info(massage);
      }

      AliRefundVo vo = new AliRefundVo();
      assert response != null;
      if(response.isSuccess()){
         //订单退款  status：0 成功 1:失败
         vo.setStatus(0);
         log.info("支付宝：支付订单支付结果查询：订单" + orderNo + "-------订单退款成功！");
      } else {
         //订单退款  status：0 成功 1:失败
         vo.setStatus(1);
         log.info("支付宝：支付订单支付结果查询：订单" + orderNo + "-------订单退款失败！");
      }
      return vo;
   }

   @Override
   public String getReturnInfo(HttpServletRequest request) {
      String payNo = request.getParameter("trade_no");
      String orderNo = request.getParameter("out_trade_no");
      String status = request.getParameter("trade_status");

      PayParamVo vo = new PayParamVo();
      PayVo payVo = new PayVo();
      if (tradeSuccess.equals(status)) {
         vo.setIsPaySuccess(0);

         //设置成功
         payVo.setMsg("SUCCESS");
      } else {
         vo.setIsPaySuccess(1);

         //支付失败
         payVo.setMsg("FAIL");
      }

      vo.setOrderNo(orderNo);
      vo.setPayNo(payNo);
      vo.setPayType(0);

      //得到回跳地址
      String returnUrl = payInfoService.updateAliPayStatus(vo);

      //本地主动去回调接口
      payVo.setOrderNo(orderNo);
      payVo.setTradeNo(payNo);

      String s = JSON.toJSONString(payVo);

      try {
         //回调接口
         HttpResult httpResult = apiService.doPost(returnUrl, s);
         log.info("---异步调用应用返回支付结果:" + httpResult);
      } catch (Exception e) {
         e.printStackTrace();
      }

      return status;
   }
}
