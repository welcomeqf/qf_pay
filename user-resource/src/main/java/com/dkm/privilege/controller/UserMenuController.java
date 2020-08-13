package com.dkm.privilege.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.privilege.entity.UserMenu;
import com.dkm.privilege.entity.vo.UserMenuPrivilegeVo;
import com.dkm.privilege.service.IUserMenuService;
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
 * @date 2020/8/12
 * @vesion 1.0
 **/
@Slf4j
@Api(tags = "菜单权限API")
@RestController
@RequestMapping("/v1/menu")
public class UserMenuController {

   @Autowired
   private IUserMenuService userMenuService;


   @ApiOperation(value = "根据ID查询菜单信息", notes = "根据ID查询菜单信息")
   @ApiImplicitParam(name = "id", value = "菜单id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/queryMenuById")
   @CrossOrigin
   @CheckToken
   public UserMenu queryMenuById (@RequestParam("id") Long id) {
      if (id == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      return userMenuService.queryMenuById(id);
   }


   @ApiOperation(value = "查询所有子权限列表以及有权限的子权限", notes = "查询所有子权限列表以及有权限的子权限")
   @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/queryMenu")
   @CrossOrigin
   @CheckToken
   public List<UserMenuPrivilegeVo> queryMenu (@RequestParam("roleId") Long roleId) {
      if (roleId == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      return userMenuService.queryMenu(roleId);
   }

   @ApiOperation(value = "查询所有父权限", notes = "查询所有父权限")
   @GetMapping("/queryParentMenu")
   @CrossOrigin
   @CheckToken
   public List<UserMenuPrivilegeVo> queryParentMenu () {
      return userMenuService.queryParentMenu();
   }
}
