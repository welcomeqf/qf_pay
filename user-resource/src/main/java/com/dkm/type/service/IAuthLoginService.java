package com.dkm.type.service;

import com.dkm.type.entity.bo.AuthLoginBO;
import com.dkm.type.entity.vo.AuthLoginQueryResultVo;
import com.dkm.type.entity.vo.AuthLoginVo;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
public interface IAuthLoginService {

   /**
    *  批量增加
    *  一个设备添加多个登录方式
    * @param bo 批量增加的参数
    */
   void addAllAuthLogin (AuthLoginBO bo);

   /**
    *  展示所有项目设备以及对应的登录方式
    * @return 返回结果
    */
   List<AuthLoginQueryResultVo> listAuthLogin ();

   /**
    *  删除该设备的登录方式
    * @param authProjectId 设备id
    * @param loginId 登录方式id
    */
   void deleteAuthLogin (Long authProjectId, Long loginId);
}
