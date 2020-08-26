package com.dkm.authUser.controller;

import com.dkm.authUser.entity.AuthInfo;
import com.dkm.authUser.entity.vo.AuthLoginVo;
import com.dkm.authUser.entity.vo.AuthRegisterVo;
import com.dkm.authUser.entity.vo.TokenResultVo;
import com.dkm.authUser.service.IAuthService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.utils.DateUtil;
import com.dkm.utils.JwtUtil;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Api(tags = "(后台)设备权限详情API")
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

   @Autowired
   private IAuthService authService;

   @ApiOperation(value = "注册设备或修改", notes = "注册设备或修改")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "id(不传id是增加设备  传id是修改设备)", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "authProjectId", value = "设备id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "appId", value = "appId", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "wxAppId", value = "微信appId", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "mchId", value = "微信商户id", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "paterNerKey", value = "微信支付密钥", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "isStopped", value = "是否停用(0-正常 1-停用)", required = true, dataType = "int", paramType = "path"),
   })
   @PostMapping("/registerAuth")
   @CrossOrigin
   public AuthLoginVo registerAuth (@RequestBody AuthRegisterVo vo) {

      if (vo.getAuthProjectId() == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      return authService.registerAuth(vo);
   }


   @ApiOperation(value = "后台项目调用*获取token", notes = "后台项目调用*获取token")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "authUserKey", value = "设备账号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "authPassword", value = "设备密码", required = true, dataType = "String", paramType = "path"),
   })
   @PostMapping("/authLogin")
   @CrossOrigin
   public TokenResultVo authLogin (@RequestBody AuthLoginVo vo) {

      if (StringUtils.isBlank(vo.getAuthPassword()) || StringUtils.isBlank(vo.getAuthUserKey())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      UserLoginQuery query = authService.authLogin(vo);

      //86400000L
      String token = JwtUtil.createjwt(1000 * 60 * 60 * 24L, query);

      TokenResultVo resultVo = new TokenResultVo();
      resultVo.setToken(token);
      LocalDateTime time = LocalDateTime.now().plusDays(1);
      resultVo.setExpTime(DateUtil.formatDateTime(time));
      return resultVo;
   }

   @ApiOperation(value = "根据authProjectId查询设备详情", notes = "根据authProjectId查询设备详情")
   @ApiImplicitParam(name = "authProjectId", value = "设备工程id", required = true, dataType = "String", paramType = "path")
   @GetMapping("/listAuth")
   @CrossOrigin
   public List<AuthInfo> listAuth (@RequestParam("authProjectId") Long authProjectId) {
      return authService.listAuth(authProjectId);
   }

   @ApiOperation(value = "查询一条设备信息", notes = "查询一条设备信息")
   @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/queryAuthOne")
   @CrossOrigin
   public AuthInfo queryAuthOne (@RequestParam("id") Long id) {
      return authService.queryAuthOne(id);
   }
}
