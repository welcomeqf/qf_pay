package com.dkm.authProject.service;

import com.dkm.authProject.entity.AuthProject;
import com.dkm.authProject.entity.bo.AuthProjectBO;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/10
 * @vesion 1.0
 **/
public interface IAuthProjectService {

   /**
    *  修改或者增加项目工程
    * @param authProjectBO 参数对象
    */
   void addOrUpdateProject (AuthProjectBO authProjectBO);

   /**
    *  展示所有的工程列表
    * @return 返回所有项目工程
    */
   List<AuthProject> listAllProject ();

   /**
    * 展示所有启用的工程列表
    * @return 返回结果
    */
   List<AuthProject> listToUserProject ();

   /**
    *  启用和禁用这个工程项目
    * @param status 0--使用 1--不使用
    * @param projectId 项目工程id
    */
   void updateStatus (Long projectId, Integer status);
}
