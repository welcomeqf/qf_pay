package com.dkm.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.user.entity.vo.UserResultVo;
import com.dkm.user.service.IUserService;
import com.dkm.user.utils.BodyUtils;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qf
 * @date 2020/8/13
 * @vesion 1.0
 **/
@Slf4j
@Api(tags = "微信登录API")
@RestController
@RequestMapping("/v1/weChat")
public class WeChatController {

   @Autowired
   private IUserService userService;

   @PostMapping("/login")
   @ApiOperation(value = "微信登录接口",notes = "传入微信登录code码，换取微信信息，和Token",produces = "application/json")
   @ApiImplicitParam(name = "code",value = "微信登录code码",dataType = "String", required = true, paramType = "body")
   public UserResultVo weChatLoginUserInfo(HttpServletRequest request){
      JSONObject json = BodyUtils.bodyJson(request);
      String code = json.getString("code");
      if (StringUtils.isBlank(code)){
         throw new ApplicationException(CodeType.SERVICE_ERROR, "参数不能为空");
      }
      return userService.weChatLoginUserInfo(code);
   }
}
