package com.dkm.role.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.role.entity.Role;
import com.dkm.role.entity.bo.RoleAddBO;
import com.dkm.role.entity.vo.RoleVo;
import com.dkm.role.service.IRoleService;
import com.dkm.type.entity.bo.AuthLoginBO;
import com.dkm.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/12
 * @vesion 1.0
 **/
@Slf4j
@Api(tags = "角色API")
@RestController
@RequestMapping("/v1/role")
public class RoleController {

   @Autowired
   private IRoleService roleService;

   @ApiOperation(value = "添加或修改角色", notes = "添加或修改角色")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "角色id(传id是修改  不传id是添加)", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "roleName", value = "角色名", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "roleStatus", value = "0--默认 1--子用户", required = true, dataType = "int", paramType = "path"),
   })
   @PostMapping("/addOrUpdateRole")
   @CrossOrigin
   @CheckToken
   public void addOrUpdateRole (@RequestBody RoleAddBO bo) {

      if (StringUtils.isBlank(bo.getRoleName()) || bo.getRoleStatus() == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      roleService.addOrUpdateRole(bo);
   }

   @ApiOperation(value = "根据角色id查询一条角色信息", notes = "根据角色id查询一条角色信息")
   @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/queryOneById")
   @CrossOrigin
   @CheckToken
   public RoleVo queryOneById (@RequestParam("roleId") Long roleId) {

      if (roleId == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      Role role = roleService.queryOneById(roleId);
      RoleVo vo = new RoleVo();
      BeanUtils.copyProperties(role, vo);
      return vo;
   }


   @ApiOperation(value = "根据角色id删除角色", notes = "根据角色id删除角色")
   @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/deleteRole")
   @CrossOrigin
   @CheckToken
   public void deleteRole (@RequestParam("roleId") Long roleId) {

      if (roleId == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      roleService.deleteRole(roleId);
   }

   @ApiOperation(value = "查询所有的角色", notes = "查询所有的角色")
   @GetMapping("/listAllRole")
   @CrossOrigin
   @CheckToken
   public List<RoleVo> listAllRole () {
      return roleService.listAllRole();
   }


   @ApiOperation(value = "查询所有启用的角色", notes = "查询所有启用的角色")
   @GetMapping("/listToUserRole")
   @CrossOrigin
   @CheckToken
   public List<RoleVo> listToUserRole () {
      return roleService.listToUserRole();
   }


   @ApiOperation(value = "启用角色/停用角色", notes = "启用角色/停用角色")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "status", value = "0--使用 1--不使用", required = true, dataType = "int", paramType = "path")
   })
   @GetMapping("/updateRoleStatus")
   @CrossOrigin
   @CheckToken
   public void updateRoleStatus (@RequestParam("roleId") Long roleId,
                                 @RequestParam("status") Integer status) {

      if (roleId == null || status == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }

      roleService.updateRoleStatus(roleId, status);
   }

}
