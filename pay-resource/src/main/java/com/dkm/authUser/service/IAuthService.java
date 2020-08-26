package com.dkm.authUser.service;

import com.dkm.authUser.entity.AuthInfo;
import com.dkm.authUser.entity.vo.AuthLoginVo;
import com.dkm.authUser.entity.vo.AuthRegisterVo;
import com.dkm.jwt.entity.UserLoginQuery;

import java.util.List;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
public interface IAuthService {

   /**
    * 注册设备
    * @param vo
    * @return
    */
   AuthLoginVo registerAuth(AuthRegisterVo vo);

   /**
    * 设备登录
    * @param vo
    * @return
    */
   UserLoginQuery authLogin(AuthLoginVo vo);

   /**
    * 查询一条记录
    * @param id
    * @return
    */
   AuthInfo queryAuthOne(Long id);

   /**
    * 查询所有设备
    * @param authProjectId 工程id
    * @return
    */
   List<AuthInfo> listAuth(Long authProjectId);

   /**
    *  停用项目工程  从而停用所有设备
    * @param authProjectId 项目工程id
    * @param status 0--正常  1--禁用
    */
   void updateToStopAuth (Long authProjectId, Integer status);

}
