package com.dkm.wxpay.controller;

import com.dkm.jwt.islogin.CheckToken;
import com.dkm.pay.entity.wxBo.WxInsertBo;
import com.dkm.pay.entity.wxBo.WxResultBo;
import com.dkm.pay.service.IPayInfoService;
import com.dkm.utils.IdGenerator;
import com.dkm.wxpay.utils.HttpRequest;
import com.dkm.wxpay.utils.WxUtils;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 商户向个人发红包
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Api(tags = "微信企业向个人发红包API")
@RestController
@RequestMapping("/v1/wxPay")
public class WxController {

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


   @ApiOperation(value = "微信企业给个人发红包", notes = "微信企业给个人发红包")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "price", value = "支付金钱", required = true, dataType = "Double", paramType = "path"),
         @ApiImplicitParam(name = "openId", value = "openId", required = true, dataType = "String", paramType = "path"),
   })
   @GetMapping("/toPerson")
   @CrossOrigin
   @CheckToken
   public Map<String, String> orders(@RequestParam("openId") String openId,
                                     @RequestParam("price") Double price) {

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
         Map<String, String> paraMap = new HashMap<String, String>();

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

         // 以下内容是返回前端页面的json数据
         Map<String,String> resultMap = new HashMap<>();
         //将返回数据XML转为Map格式
         Map<String, String> result = WXPayUtil.xmlToMap(xmlStr);

         if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code")) ) {
            resultMap.put("订单号", result.get("partner_trade_no"));
            resultMap.put("微信付款单号", result.get("payment_no"));
            resultMap.put("付款时间",result.get("payment_time"));
            resultMap.put("status","0");
            return resultMap;
         } else {
            resultMap.put("return_msg",result.get("return_msg"));
            resultMap.put("status","1");
            return resultMap;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
}
