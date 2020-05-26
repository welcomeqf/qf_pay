package com.dkm.alipay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.pay.entity.PayInfo;
import com.dkm.pay.entity.vo.PayReturnVo;
import com.dkm.pay.service.IPayInfoService;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Api
@Slf4j
@RestController
@RequestMapping("/v1/ali")
public class AliPayPcController {

   @Autowired
   private IPayInfoService payInfoService;

   @Value("${pay.testUrl}")
   private String url;

   @Value("${pay.aliPrivateKey}")
   private String privateKey;

   @Value("${pay.aliPublicKey}")
   private String aliPublicKey;

   @Value("${pay.notifyServerUrl}")
   private String notifyServerUrl;


   @ApiOperation(value = "支付宝支付", notes = "支付宝支付")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "price", value = "支付金钱", required = true, dataType = "Double", paramType = "path"),
         @ApiImplicitParam(name = "subject", value = "订单标题", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "body", value = "订单描述", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "returnUrl", value = "前端跳转页面", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "notifyUrl", value = "后端回调接口", required = true, dataType = "String", paramType = "path"),
   })
   @GetMapping("/payAli")
   @CheckToken
   @CrossOrigin
   public void aliPcPay (@RequestParam("orderNo") String orderNo,
                         @RequestParam("price") Double price,
                         @RequestParam("subject") String subject,
                         @RequestParam("body") String body,
                         @RequestParam("returnUrl") String returnUrl,
                         @RequestParam("notifyUrl") String notifyUrl,
                         HttpServletResponse httpResponse) throws IOException {

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
      httpResponse.getWriter().write(form);
      httpResponse.getWriter().flush();
      httpResponse.getWriter().close();
   }


   @ApiOperation(value = "支付宝支付(退款)查询接口", notes = "支付宝支付(退款)查询接口")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "queryType", value = "(查询类型 0-支付状况 1-退款状态)", required = true, dataType = "int", paramType = "path"),
   })
   @GetMapping("/getPayInfo")
   @CheckToken
   @CrossOrigin
   public Object queryPay (@RequestParam("orderNo") String orderNo,
                         @RequestParam("queryType") Integer queryType) {

      if (StringUtils.isBlank(orderNo) || queryType == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      //查询appId
      String appId = payInfoService.queryAppId();

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


   @ApiOperation(value = "支付宝退款接口", notes = "支付宝退款接口")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "money", value = "退款金额", required = true, dataType = "double", paramType = "path"),
   })
   @GetMapping("/refundAli")
   @CheckToken
   @CrossOrigin
   public Map<String,Integer> alipayCreateOrderRefund(@RequestParam("orderNo") String orderNo,
                                                      @RequestParam("money") Double money) {
      HashMap<String,Integer> map = new HashMap<>(1);

      String appId = payInfoService.queryAppId();

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
         String massage = "alipay.trade.refund退款接口：订单签名错误";
         log.info(massage);
      }
      if(response.isSuccess()){
         //订单退款  status：0 成功 1:失败
         map.put("status", 0);
         log.info("支付宝：支付订单支付结果查询：订单" + orderNo + "-------订单退款成功！");
      } else {
         //订单退款  status：0 成功 1:失败
         map.put("status",1);
         log.info("支付宝：支付订单支付结果查询：订单" + orderNo + "-------订单退款失败！");
      }
      return map;
   }
}
