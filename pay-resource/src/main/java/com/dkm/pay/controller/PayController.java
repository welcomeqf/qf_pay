package com.dkm.pay.controller;

import com.dkm.alipay.service.IAliPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
   private IAliPayService aliPayService;


   @PostMapping("/updateInfo")
   @CrossOrigin
   public String getReturnInfo (HttpServletRequest request) {
      //支付宝回调接口
      return aliPayService.getReturnInfo(request);
   }
}
