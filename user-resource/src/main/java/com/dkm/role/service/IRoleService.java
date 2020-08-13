package com.dkm.role.service;

import com.dkm.role.entity.Role;
import com.dkm.role.entity.bo.RoleAddBO;
import com.dkm.role.entity.vo.RoleVo;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
public interface IRoleService {

   /**
    * 添加或修改角色
    * @param roleAddBO 角色的参数 传id为修改 不传id为添加
    */
   void addOrUpdateRole (RoleAddBO roleAddBO);

   /**
    *  根据角色id查询一条角色信息
    * @param roleId 角色id
    * @return 返回角色信息
    */
   Role queryOneById (Long roleId);

   /**
    *  根据角色id删除角色
    * @param roleId 角色Id
    */
   void deleteRole (Long roleId);

   /**
    *  查询所有的角色
    * @return 返回停用以及使用的角色
    */
   List<RoleVo> listAllRole ();

   /**
    *  启用角色/停用角色
    * @param roleId 角色id
    * @param status 0--使用 1--不使用
    */
   void updateRoleStatus (Long roleId, Integer status);

   /**
    *  查询所有启用的角色
    * @return 返回正在使用的角色
    */
   List<RoleVo> listToUserRole ();
}
