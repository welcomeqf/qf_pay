package com.dkm.authProject.controller;

import com.dkm.authProject.entity.AuthProject;
import com.dkm.authProject.entity.bo.AuthProjectBO;
import com.dkm.authProject.service.IAuthProjectService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
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
 * @date 2020/8/10
 * @vesion 1.0
 **/
@Slf4j
@RestController
@Api(tags = "(后台)项目工程的Api")
@RequestMapping("/v1/project")
public class AuthProjectController {

   @Autowired
   private IAuthProjectService authProjectService;


   @ApiOperation(value = "修改或者增加项目工程", notes = "修改或者增加项目工程")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "id(不传id是增加项目  传id是修改项目)", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "authName", value = "项目名称", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "authDescribe", value = "项目描述", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "authStatus", value = "0--展示支付填写信息 1--不展示支付填写信息(用来给前端做判断)", required = true, dataType = "int", paramType = "path"),
         })
   @PostMapping("/addOrUpdateProject")
   @CrossOrigin
   public void addOrUpdateProject (@RequestBody AuthProjectBO bo) {

      if (StringUtils.isBlank(bo.getAuthName()) || bo.getAuthStatus() == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      authProjectService.addOrUpdateProject(bo);
   }


   @ApiOperation(value = "展示所有启用的工程列表", notes = "展示所有启用的工程列表")
   @GetMapping("/listToUserProject")
   @CrossOrigin
   public List<AuthProject> listToUserProject () {
      return authProjectService.listToUserProject();
   }


   @ApiOperation(value = "展示所有的工程列表", notes = "展示所有的工程列表")
   @GetMapping("/listAllProject")
   @CrossOrigin
   public List<AuthProject> listAllProject () {
      return authProjectService.listAllProject();
   }

   @ApiOperation(value = "启用和禁用这个工程项目", notes = "启用和禁用这个工程项目")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "projectId", value = "项目工程id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "status", value = "0--使用 1--不使用", required = true, dataType = "int", paramType = "path"),
         })
   @GetMapping("/updateStatus")
   @CrossOrigin
   public void updateStatus (@RequestParam("projectId") Long projectId, @RequestParam("status") Integer status) {

      if (projectId == null || status == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

     authProjectService.updateStatus(projectId, status);
   }
}
