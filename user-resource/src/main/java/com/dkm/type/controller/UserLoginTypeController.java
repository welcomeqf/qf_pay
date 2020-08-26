package com.dkm.type.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.type.entity.UserLoginType;
import com.dkm.type.entity.bo.UserLoginTypeBO;
import com.dkm.type.service.IUserLoginTypeService;
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
@Api(tags = "(后台)登录方式API")
@RestController
@RequestMapping("/v1/loginType")
public class UserLoginTypeController {

   @Autowired
   private IUserLoginTypeService userLoginTypeService;

   @ApiOperation(value = "增加或者修改登录方式", notes = "增加或者修改登录方式")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "id(不传id是增加项目  传id是修改项目)", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "loginType", value = "0--账号密码登录 1--微信登录(后续可加)", required = true, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "loginName", value = "登录方式的名称", required = true, dataType = "String", paramType = "path"),
         })
   @PostMapping("/addOrUpdateLoginType")
   @CrossOrigin
   public void addOrUpdateLoginType (@RequestBody UserLoginTypeBO bo) {

      if (StringUtils.isBlank(bo.getLoginName()) || bo.getLoginType() == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      userLoginTypeService.addOrUpdateLoginType(bo);
   }

   @ApiOperation(value = "展示所有登录方式", notes = "展示所有登录方式")
   @GetMapping("/listAllLoginType")
   @CrossOrigin
   public List<UserLoginType> listAllLoginType () {
      return userLoginTypeService.listAllLoginType();
   }

   @ApiOperation(value = "返回所有启用的登录方式", notes = "返回所有启用的登录方式")
   @GetMapping("/listToUserLoginType")
   @CrossOrigin
   public List<UserLoginType> listToUserLoginType () {
      return userLoginTypeService.listToUserLoginType();
   }


   @ApiOperation(value = "启用或停用登录方式", notes = "启用或停用登录方式")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "loginTypeId", value = "登录方式id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "status", value = "0--使用  1--不使用", required = true, dataType = "int", paramType = "path"),
   })
   @GetMapping("/updateLoginTypeStatus")
   @CrossOrigin
   public void updateLoginTypeStatus (@RequestParam("loginTypeId") Long loginTypeId, @RequestParam("status") Integer status) {

      if (loginTypeId == null || status == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      userLoginTypeService.updateLoginTypeStatus(loginTypeId, status);
   }

}
