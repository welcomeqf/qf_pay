package com.dkm.privilege.service;

import com.dkm.jwt.entity.PrivilegeMenuQuery;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
public interface IPrivilegeService {

   /**
    * 一次性增加一个角色的所有权限
    * @param roleId 角色id
    * @param menuList 菜单id集合
    */
   void insertAllPrivilege(Long roleId, List<Long> menuList);

   /**
    * 根据角色查询所有菜单权限
    * @param roleId 角色id
    * @return
    */
   List<PrivilegeMenuQuery> queryAllMenu(Long roleId);
}
