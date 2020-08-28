package com.dkm.user.service;

import com.dkm.user.entity.bo.UserBO;
import com.dkm.user.entity.bo.UserInfoBO;
import com.dkm.user.entity.bo.UserLoginBO;
import com.dkm.user.entity.vo.UserResultVo;
import com.dkm.user.entity.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * @author qf
 * @date 2020/8/13
 * @vesion 1.0
 **/
public interface IUserService {

   /**
    *  注册 （系统管理员分配注册）
    *  （用户自己注册信息）
    * @param userBO
    */
   void addUser (UserBO userBO);

   /**
    *  用户账号密码登录
    * @param bo 登录的参数
    * @return 返回token信息
    */
   UserResultVo loginUser (UserLoginBO bo);

   /**
    *  完善用户信息
    * @param bo 用户信息的参数
    */
   void perfectUser (UserInfoBO bo);

   /**
    *  微信登录
    * @param code 微信登录的授权code
    * @param key 创建token的key
    * @return 返回token
    */
   UserResultVo weChatLoginUserInfo(String code, String key);

   /**
    *  查询一条用户信息
    * @param userId 用户id
    * @return 返回一条用户信息
    */
   UserVo queryOne (Long userId);

   /**
    *  展示该角色下的所有用户
    * @param roleId 角色id
    * @return 返回用户列表
    */
   List<UserVo> listUserByRole (Long roleId);
}
