package com.dkm.privilege.service;

import com.dkm.privilege.entity.UserMenu;
import com.dkm.privilege.entity.vo.MenuResultVo;
import com.dkm.privilege.entity.vo.UserMenuPrivilegeVo;
import com.dkm.privilege.entity.vo.UserMenuVo;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
public interface IUserMenuService {

   /**
    *  查询所有的菜单
    *  内部调用
    * @param list 菜单id的集合
    * @return 返回菜单的列表
    */
   List<UserMenu> listAllMenu (List<Long> list);

   /**
    *  查询所有菜单
    * @param parentId 菜单的父id 不传则是查询所有父菜单的信息  传了则是查询所有子菜单的信息
    * @return 返回菜单结果
    */
   List<UserMenuVo> queryAllMenu (Long parentId);

   /**
    * 根据ID查询菜单信息
    * @param id 菜单id
    * @return 返回菜单具体结果
    */
   UserMenu queryMenuById(Long id);

   /**
    *  根据角色id查询所有菜单
    * @param roleId 角色id
    * @return 返回该角色下有无权限的菜单
    */
   List<UserMenuPrivilegeVo> queryMenuByRoleId (Long roleId);

   /**
    *  查询所有子权限列表以及有权限的子权限
    * @param roleId 角色id
    * @return 返回所有有权限的子菜单
    */
   List<UserMenuPrivilegeVo> queryMenu (Long roleId);

   /**
    *  查询所有父权限
    * @return 返回所有父权限菜单
    */
   List<UserMenuPrivilegeVo> queryParentMenu ();

}
