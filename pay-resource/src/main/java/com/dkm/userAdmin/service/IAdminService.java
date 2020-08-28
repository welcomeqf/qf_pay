package com.dkm.userAdmin.service;

import com.dkm.userAdmin.entity.UserAdmin;
import com.dkm.userAdmin.entity.vo.LoginVo;

import java.util.List;

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
   UserAdmin loginAdmin(String userName, String password);

   /**
    *  展示所有用户
    * @return 返回
    */
   List<UserAdmin> listUserAdmin ();
}
