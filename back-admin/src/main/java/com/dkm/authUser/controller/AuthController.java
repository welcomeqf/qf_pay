package com.dkm.authUser.controller;

import com.dkm.authUser.entity.AuthInfo;
import com.dkm.authUser.entity.vo.AuthLoginVo;
import com.dkm.authUser.entity.vo.AuthRegisterVo;
import com.dkm.authUser.entity.vo.TokenResultVo;
import com.dkm.authUser.service.IAuthService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.utils.JwtUtil;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Api(tags = "设备权限API")
@Controller
@RequestMapping("/v1/auth")
public class AuthController {

   @Autowired
   private IAuthService authService;

   @ApiOperation(value = "注册设备或修改", notes = "注册设备或修改")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "id", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "authName", value = "设备名称", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "authDescribe", value = "设备描述", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "appId", value = "appId", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "wxAppId", value = "微信appId", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "mchId", value = "微信商户id", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "paterNerKey", value = "微信支付密钥", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "isStopped", value = "是否停用(0-正常 1-停用)", required = true, dataType = "int", paramType = "path"),
   })
   @PostMapping("/registerAuth")
   public String registerAuth (AuthRegisterVo vo, Model model) {

      if (StringUtils.isBlank(vo.getAuthName())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      AuthLoginVo authLoginVo = authService.registerAuth(vo);

      model.addAttribute("authUserKey",authLoginVo.getAuthUserKey());
      model.addAttribute("authPassword",authLoginVo.getAuthPassword());

      return "success";
   }


   @ApiOperation(value = "登录设备", notes = "登录设备")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "authUserKey", value = "设备账号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "authPassword", value = "设备密码", required = true, dataType = "String", paramType = "path"),
   })
   @PostMapping("/authLogin")
   public TokenResultVo authLogin (@RequestBody AuthLoginVo vo) {

      if (StringUtils.isBlank(vo.getAuthPassword()) || StringUtils.isBlank(vo.getAuthUserKey())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      UserLoginQuery query = authService.authLogin(vo);

      //86400000L
      String token = JwtUtil.createJWT(1000 * 60 * 60 * 24L, query);

      TokenResultVo resultVo = new TokenResultVo();
      resultVo.setToken(token);
      resultVo.setExp("24小时");
      return resultVo;
   }

   @ApiOperation(value = "查询所有设备", notes = "查询所有设备")
   @GetMapping("/listAuth")
   public String listAuth (Model model) {
      List<AuthInfo> list = authService.listAuth();
      model.addAttribute("list",list);
      return "home";
   }

   @ApiOperation(value = "查询一条设备信息", notes = "查询一条设备信息")
   @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/queryAuthOne")
   public AuthInfo queryAuthOne (@RequestParam("id") Long id) {
      return authService.queryAuthOne(id);
   }
}
