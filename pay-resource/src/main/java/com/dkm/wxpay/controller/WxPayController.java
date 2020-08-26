package com.dkm.wxpay.controller;


import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.utils.StringUtils;
import com.dkm.wxpay.entity.WxJsApiPayResultVo;
import com.dkm.wxpay.entity.WxPayResultVo;
import com.dkm.wxpay.entity.WxRefundVo;
import com.dkm.wxpay.service.IWxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qf
 * @date 2020/3/19
 * @vesion 1.0
 **/
@Api(tags = "微信支付API")
@Slf4j
@RestController
@RequestMapping("/v1/wx")
public class WxPayController {

   @Autowired
   private IWxService wxService;

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
   public WxPayResultVo wxAppPay(@RequestParam("body") String body,
                                 @RequestParam("orderNo") String orderNo,
                                 @RequestParam("price") Double price,
                                 @RequestParam("notifyUrl") String notifyUrl) {

      if (StringUtils.isBlank(body) || StringUtils.isBlank(orderNo) || price == null || StringUtils.isBlank(notifyUrl)) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      return wxService.wxAppPay(body,orderNo,price,notifyUrl);
   }


   @ApiOperation(value = "微信支付(退款)查询接口", notes = "微信支付(退款)查询接口")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "queryType", value = "(查询类型 0-支付状况 1-退款状态)", required = true, dataType = "int", paramType = "path"),
   })
   @GetMapping("/queryPay")
   @CheckToken
   @CrossOrigin
   public Object queryPay (@RequestParam("orderNo") String orderNo,
                           @RequestParam("queryType") Integer queryType) {

     if (StringUtils.isBlank(orderNo) || queryType == null) {
        throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
     }

      return wxService.wxPayRefundResultQuery (orderNo, queryType);
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
   public WxRefundVo refundWxPay (@RequestParam("orderNo") String orderNo,
                                  @RequestParam("refundPrice") Double refundPrice,
                                  @RequestParam("orderPrice") Double orderPrice) {

      if (StringUtils.isBlank(orderNo) || refundPrice == null || orderPrice == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
     return wxService.refundWxPay (orderNo, refundPrice, orderPrice);
   }


   @RequestMapping("/notify")
   @CrossOrigin
   public void callBack(HttpServletRequest request, HttpServletResponse response) {
      //微信回调接口
      wxService.callBack (request, response);
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
   public WxJsApiPayResultVo pay (@RequestParam("body") String body,
                                  @RequestParam("orderNo") String orderNo,
                                  @RequestParam("price") Double price,
                                  @RequestParam("notifyUrl") String notifyUrl,
                                  @RequestParam("openId") String openId) {

      if (StringUtils.isBlank(body) || StringUtils.isBlank(orderNo) || price == null
      || StringUtils.isBlank(notifyUrl) || StringUtils.isBlank(openId)) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      return wxService.wxJsApiPay (body, orderNo, price, notifyUrl, openId);

   }


}
