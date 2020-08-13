package com.dkm.privilege.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.entity.PrivilegeMenuQuery;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.privilege.entity.bo.UserPrivilegeBO;
import com.dkm.privilege.service.IPrivilegeService;
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
@Api(tags = "权限API")
@RestController
@RequestMapping("/v1/privilege")
public class PrivilegeController {

   @Autowired
   private IPrivilegeService privilegeService;

   @ApiOperation(value = "增加或修改权限", notes = "增加或修改权限")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "menuList", value = "所有菜单id的数组", required = true, dataType = "List", paramType = "path")
   })
   @PostMapping("/insertPrivilege")
   @CrossOrigin
   @CheckToken
   public void insertPrivilege(@RequestBody UserPrivilegeBO bo) {
      if (bo.getRoleId() == null || bo.getMenuList() == null || bo.getMenuList().size() == 0) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      privilegeService.insertAllPrivilege(bo.getRoleId(),bo.getMenuList());
   }

   @ApiOperation(value = "上线就调用的权限接口", notes = "上线就调用的权限接口")
   @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/queryAllMenu")
   @CrossOrigin
   @CheckToken
   public List<PrivilegeMenuQuery> queryAllMenu(@RequestParam("roleId") Long roleId) {
      if (roleId == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      return privilegeService.queryAllMenu(roleId);
   }
}
