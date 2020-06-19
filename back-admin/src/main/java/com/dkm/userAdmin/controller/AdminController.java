package com.dkm.userAdmin.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.userAdmin.entity.vo.AdminVo;
import com.dkm.userAdmin.entity.vo.LoginVo;
import com.dkm.userAdmin.entity.vo.ResultVo;
import com.dkm.userAdmin.service.IAdminService;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author qf
 * @date 2020/3/25
 * @vesion 1.0
 **/
@Api
@Slf4j
@Controller
@RequestMapping("/v1/admin")
public class AdminController {

   @Autowired
   private IAdminService adminService;

   @ApiOperation(value = "注册", notes = "注册")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
   })
   @PostMapping("/addAdmin")
   public String addAdmin (AdminVo vo) {

      if (StringUtils.isBlank(vo.getUserName()) || StringUtils.isBlank(vo.getPassword())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      LoginVo loginVo = adminService.addAdmin(vo.getUserName(), vo.getPassword());

      return "index";
   }


   @ApiOperation(value = "登录", notes = "登录")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
   })
   @PostMapping("/loginAdmin")
   public String loginAdmin (AdminVo vo) {

      if (StringUtils.isBlank(vo.getUserName()) || StringUtils.isBlank(vo.getPassword())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

       adminService.loginAdmin(vo.getUserName(), vo.getPassword());

      return "adminIndex";

   }

}
