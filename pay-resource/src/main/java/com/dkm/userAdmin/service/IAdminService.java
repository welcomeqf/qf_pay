package com.dkm.userAdmin.service;

import com.dkm.userAdmin.entity.vo.LoginVo;

/**
 * @author qf
 * @date 2020/3/25
 * @vesion 1.0
 **/
public interface IAdminService {

   /**
    * 添加
    * @param userName
    * @param password
    * @return
    */
   LoginVo addAdmin(String userName, String password);

   /**
    * 登录
    * @param userName
    * @param password
    */
   void loginAdmin(String userName, String password);
}
