package com.dkm.user.controller;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.role.entity.bo.RoleAddBO;
import com.dkm.user.entity.bo.UserBO;
import com.dkm.user.entity.bo.UserInfoBO;
import com.dkm.user.entity.bo.UserLoginBO;
import com.dkm.user.entity.vo.UserResultVo;
import com.dkm.user.entity.vo.UserVo;
import com.dkm.user.service.IUserService;
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
 * @date 2020/8/13
 * @vesion 1.0
 **/
@Slf4j
@Api(tags = "用户API")
@RestController
@RequestMapping("/v1/user")
public class UserController {

   @Autowired
   private IUserService userService;

   @ApiOperation(value = "注册(系统管理员分配注册/用户自己注册信息)", notes = "注册(系统管理员分配注册/用户自己注册信息)")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "userRoleId", value = "角色id", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "companyId", value = "公司id", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "account", value = "账号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "cName", value = "昵称", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "userSex", value = "0--男 1--女", required = false, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "userAge", value = "年龄", required = false, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "userCard", value = "身份证", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "isStopped", value = "是否使用 0-使用 1--停用", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "userStatus", value = "0--用户  1--子用户", required = false, dataType = "int", paramType = "path"),
   })
   @PostMapping("/addUser")
   @CrossOrigin
   @CheckToken
   public void addUser (@RequestBody UserBO bo) {
      if (StringUtils.isBlank(bo.getAccount()) || StringUtils.isBlank(bo.getPassword())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "账号或者密码不能为空");
      }
      userService.addUser(bo);
   }

   @ApiOperation(value = "用户账号密码登录", notes = "用户账号密码登录")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "userName", value = "账号", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "key", value = "创建token的key", required = true, dataType = "String", paramType = "path")
        })
   @PostMapping("/loginUser")
   @CrossOrigin
   @CheckToken
   public UserResultVo loginUser (@RequestBody UserLoginBO bo) {
      if (StringUtils.isBlank(bo.getUserName()) || StringUtils.isBlank(bo.getPassword()) || StringUtils.isBlank(bo.getKey())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      return userService.loginUser(bo);
   }


   @ApiOperation(value = "完善用户信息", notes = "完善用户信息")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "userRoleId", value = "角色id", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "companyId", value = "公司id", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "eName", value = "英文名", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "userImgUrl", value = "用户头像", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "cName", value = "昵称", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "userSex", value = "0--男 1--女", required = false, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "userAge", value = "年龄", required = false, dataType = "int", paramType = "path"),
         @ApiImplicitParam(name = "userCard", value = "身份证", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "userTel", value = "电话号码", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "isStopped", value = "是否使用 0-使用 1--停用", required = false, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "userStatus", value = "0--用户  1--子用户", required = false, dataType = "int", paramType = "path"),
   })
   @PostMapping("/perfectUser")
   @CrossOrigin
   @CheckToken
   public void perfectUser (@RequestBody UserInfoBO bo) {

      if (bo.getId() == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "用户id不能为空");
      }
      userService.perfectUser(bo);
   }

   @ApiOperation(value = "查询一条用户信息", notes = "查询一条用户信息")
   @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/queryOne")
   @CrossOrigin
   @CheckToken
   public UserVo queryOne (@RequestParam("userId") Long userId) {
      if (userId == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      return userService.queryOne(userId);
   }


   @ApiOperation(value = "展示该角色下的所有用户", notes = "展示该角色下的所有用户")
   @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/listUserByRole")
   @CrossOrigin
   @CheckToken
   public List<UserVo> listUserByRole (@RequestParam("roleId") Long roleId) {
      if (roleId == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      return userService.listUserByRole(roleId);
   }
}
