package com.dkm.type.service;

import com.dkm.type.entity.UserLoginType;
import com.dkm.type.entity.bo.UserLoginTypeBO;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/10
 * @vesion 1.0
 **/
public interface IUserLoginTypeService {

   /**
    *  增加或者修改登录方式
    * @param userLoginTypeBO 参数对象
    */
   void addOrUpdateLoginType (UserLoginTypeBO userLoginTypeBO);

   /**
    *  展示所有登录方式
    * @return 返回结果
    */
   List<UserLoginType> listAllLoginType ();

   /**
    *  返回所有启用的登录方式
    * @return 返回结果
    */
   List<UserLoginType> listToUserLoginType ();

   /**
    *  启用或停用
    * @param loginTypeId 登录方式id
    * @param status 0--使用  1--不使用
    */
   void updateLoginTypeStatus (Long loginTypeId, Integer status);
}
