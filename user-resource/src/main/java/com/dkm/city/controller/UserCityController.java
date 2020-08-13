package com.dkm.city.controller;

import com.dkm.city.entity.UserCity;
import com.dkm.city.entity.query.UserCityQuery;
import com.dkm.city.service.IUserCityService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.islogin.CheckToken;
import com.dkm.privilege.entity.bo.UserPrivilegeBO;
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
 * @date 2020/8/12
 * @vesion 1.0
 **/
@Slf4j
@Api(tags = "城市Api")
@RestController
@RequestMapping("/v1/city")
public class UserCityController {

   @Autowired
   private IUserCityService userCityService;

   @ApiOperation(value = "添加或者修改城市", notes = "添加或者修改城市")
   @ApiImplicitParams({
         @ApiImplicitParam(name = "id", value = "(不传则是添加  传则是修改)", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "cityName", value = "城市名称", required = true, dataType = "String", paramType = "path"),
         @ApiImplicitParam(name = "userId", value = "用户id", required = false, dataType = "Long", paramType = "path"),
         @ApiImplicitParam(name = "cityStatus", value = "0--系统城市 1--用户的属于城市", required = true, dataType = "int", paramType = "path")
   })
   @PostMapping("/addOrUpdateCity")
   @CrossOrigin
   @CheckToken
   public void addOrUpdateCity(@RequestBody UserCity bo) {
      if (bo.getCityStatus() == null || StringUtils.isBlank(bo.getCityName())) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      userCityService.addOrUpdateCity(bo);
   }


   @ApiOperation(value = "根据城市id删除城市", notes = "根据城市id删除城市")
   @ApiImplicitParam(name = "cityId", value = "城市id", required = true, dataType = "Long", paramType = "path")
   @GetMapping("/deleteCity")
   @CrossOrigin
   @CheckToken
   public void deleteCity(@RequestParam("cityId") Long cityId) {
      if (cityId == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "参数不能为空");
      }
      userCityService.deleteCity(cityId);
   }

   @ApiOperation(value = "查询城市", notes = "查询城市")
   @ApiImplicitParam(name = "userId", value = "传null--系统城市 传用户id--用户的属于城市", required = false, dataType = "Long", paramType = "path")
   @GetMapping("/queryCityList")
   @CrossOrigin
   @CheckToken
   public List<UserCityQuery> queryCityList(@RequestParam("userId") Long userId) {
      return userCityService.queryCityList(userId);
   }
}
