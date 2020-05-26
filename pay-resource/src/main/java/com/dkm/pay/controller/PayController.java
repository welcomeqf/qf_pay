package com.dkm.pay.controller;

import com.alibaba.fastjson.JSON;
import com.dkm.httpclient.HttpClientUtils;
import com.dkm.httpclient.HttpResult;
import com.dkm.pay.entity.vo.PayParamVo;
import com.dkm.pay.entity.vo.PayVo;
import com.dkm.pay.service.IPayInfoService;
import com.dkm.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qf
 * @date 2020/3/17
 * @vesion 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/returnPay")
public class PayController {

   @Autowired
   private IPayInfoService payInfoService;

   @Autowired
   private HttpClientUtils apiService;

   /**
    * 支付宝回调接口
    * @param request
    */
   @PostMapping("/updateInfo")
   @CrossOrigin
   public String getReturnInfo (HttpServletRequest request) {

      String payNo = request.getParameter("trade_no");
      String orderNo = request.getParameter("out_trade_no");
      String status = request.getParameter("trade_status");

      PayParamVo vo = new PayParamVo();
      PayVo payVo = new PayVo();
      if ("TRADE_SUCCESS".equals(status)) {
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
         HttpResult httpResult = apiService.doPost(returnUrl, s);
         log.info("---异步调用应用返回支付结果:" + httpResult);
      } catch (Exception e) {
         e.printStackTrace();
      }

      return status;
   }
}
