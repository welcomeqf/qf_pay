package com.dkm.wxpay.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.pay.entity.wxBo.WxInsertBo;
import com.dkm.pay.entity.wxBo.WxResultBo;
import com.dkm.pay.service.IPayInfoService;
import com.dkm.utils.IdGenerator;
import com.dkm.utils.StringUtils;
import com.dkm.wxpay.entity.WxResultVo;
import com.dkm.wxpay.service.IWxService;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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

   @Autowired
   private IWxService wxService;


   @ApiOperation(value = "微信企业给个人发红包", notes = "微信企业给个人发红包")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "price", value = "支付金钱", required = true, dataType = "Double", paramType = "path"),
         @ApiImplicitParam(name = "openId", value = "openId", required = true, dataType = "String", paramType = "path"),
   })
   @GetMapping("/toPerson")
   @CrossOrigin
   @CheckToken
   public WxResultVo orders(@RequestParam("openId") String openId,
                            @RequestParam("price") Double price) {

      if (StringUtils.isBlank(openId) || price == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      return wxService.companyToPerson(openId, price);
   }
}
