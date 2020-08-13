package com.dkm.type.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.type.entity.UserLoginType;
import com.dkm.type.entity.bo.AuthLoginBO;
import com.dkm.type.entity.bo.UserLoginTypeBO;
import com.dkm.type.entity.vo.AuthLoginQueryResultVo;
import com.dkm.type.service.IAuthLoginService;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Slf4j
@Api(tags = "设备对应的登录方式Api")
@RestController
@RequestMapping("/v1/authLogin")
public class AuthLoginController {

   @Autowired
   private IAuthLoginService authLoginService;

   @ApiOperation(value = "批量增加(一个设备添加多个登录方式)", notes = "批量增加(一个设备添加多个登录方式)")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "authProjectId", value = "设备工程id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "list", value = "登录方式id的数组", required = true, dataType = "list", paramType = "path"),
         })
   @PostMapping("/addAllAuthLogin")
   @CrossOrigin
   public void addAllAuthLogin (@RequestBody AuthLoginBO bo) {

      if (bo.getAuthProjectId() == null || bo.getList() == null || bo.getList().size() == 0) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      authLoginService.addAllAuthLogin(bo);
   }

   @ApiOperation(value = "展示所有项目设备以及对应的登录方式", notes = "展示所有项目设备以及对应的登录方式")
   @GetMapping("/listAuthLogin")
   @CrossOrigin
   public List<AuthLoginQueryResultVo> listAuthLogin () {
      return authLoginService.listAuthLogin();
   }

   @ApiOperation(value = "删除该设备的登录方式", notes = "删除该设备的登录方式")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "authProjectId", value = "设备工程id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "loginId", value = "登录方式id", required = true, dataType = "Long", paramType = "path"),
   })
   @GetMapping("/deleteAuthLogin")
   @CrossOrigin
   public void deleteAuthLogin (@RequestParam("authProjectId") Long authProjectId, @RequestParam("loginId") Long loginId) {

      if (authProjectId == null || loginId == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      authLoginService.deleteAuthLogin(authProjectId, loginId);
   }
}
