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
   AuthLoginVo registerAuth (AuthRegisterVo vo);

   /**
    * 设备登录
    * @param vo
    * @return
    */
   UserLoginQuery authLogin (AuthLoginVo vo);

   /**
    * 查询一条记录
    * @param id
    * @return
    */
   AuthInfo queryAuthOne (Long id);

   /**
    * 查询所有设备
    * @return
    */
   List<AuthInfo> listAuth ();

}
