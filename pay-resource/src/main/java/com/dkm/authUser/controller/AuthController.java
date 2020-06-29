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

   @ApiOperation(value = "查询一条设备信息", notes = "查询一条设备信息")
   @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/queryAuthOne")
   public AuthInfo queryAuthOne (@RequestParam("id") Long id) {
      return authService.queryAuthOne(id);
   }
}
