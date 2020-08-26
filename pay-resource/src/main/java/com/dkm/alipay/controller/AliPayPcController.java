package com.dkm.alipay.controller;


import com.dkm.alipay.entity.AliRefundVo;
import com.dkm.alipay.service.IAliPayService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Api(tags = "支付宝支付API")
@Slf4j
@RestController
@RequestMapping("/v1/ali")
public class AliPayPcController {

   @Autowired
   private IAliPayService aliPayService;


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
                         HttpServletResponse httpResponse) {

      if (StringUtils.isBlank(orderNo) || price == null || StringUtils.isBlank(subject)
      || StringUtils.isBlank(body) || StringUtils.isBlank(returnUrl) || StringUtils.isBlank(notifyUrl)) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      //支付宝支付
      aliPayService.aliPcPay(orderNo, price, subject, body, returnUrl, notifyUrl, httpResponse);
   }


   @ApiOperation(value = "支付宝支付(退款)查询接口", notes = "支付宝支付(退款)查询接口")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "queryType", value = "查询类型 0-支付状况 1-退款状态", required = true, dataType = "int", paramType = "path"),
   })
   @GetMapping("/getPayInfo")
   @CheckToken
   @CrossOrigin
   public Object queryPay (@RequestParam("orderNo") String orderNo,
                         @RequestParam("queryType") Integer queryType) {

      if (StringUtils.isBlank(orderNo) || queryType == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      //查询支付或退款结果
      return aliPayService.queryPay(orderNo, queryType);
   }


   @ApiOperation(value = "支付宝退款接口", notes = "支付宝退款接口")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "money", value = "退款金额", required = true, dataType = "double", paramType = "path"),
   })
   @GetMapping("/refundAli")
   @CheckToken
   @CrossOrigin
   public AliRefundVo aliPayCreateOrderRefund(@RequestParam("orderNo") String orderNo,
                                              @RequestParam("money") Double money) {

      if (StringUtils.isBlank(orderNo) || money == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      return aliPayService.aliPayCreateOrderRefund(orderNo, money);
   }
}
