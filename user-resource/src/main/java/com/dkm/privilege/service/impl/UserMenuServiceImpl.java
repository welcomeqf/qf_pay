package com.dkm.privilege.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.PrivilegeMenuQuery;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.privilege.dao.UserMenuMapper;
import com.dkm.privilege.entity.UserMenu;
import com.dkm.privilege.entity.vo.MenuResultVo;
import com.dkm.privilege.entity.vo.UserMenuPrivilegeVo;
import com.dkm.privilege.entity.vo.UserMenuVo;
import com.dkm.privilege.service.IPrivilegeService;
import com.dkm.privilege.service.IUserMenuService;
import com.dkm.type.entity.vo.AuthLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserMenuServiceImpl extends ServiceImpl<UserMenuMapper, UserMenu> implements IUserMenuService {

   @Autowired
   private LocalUser localUser;

   @Autowired
   private IPrivilegeService privilegeService;

   @Override
   public List<UserMenu> listAllMenu(List<Long> list) {
      //查询所有菜单
      //根据菜单id的集合
      return baseMapper.selectBatchIds(list);
   }

   @Override
   public List<UserMenuVo> queryAllMenu(Long parentId) {
      UserLoginQuery auth = localUser.getUser();
      List<UserMenuVo> list = new ArrayList<>();
      //查询所有系统功能
      if (parentId == 0) {
         LambdaQueryWrapper<UserMenu> queryWrapper = new LambdaQueryWrapper<UserMenu>()
               .eq(UserMenu::getParentId,0)
               .eq(UserMenu::getAuthId,auth.getId());
         List<UserMenu> userMenus = baseMapper.selectList(queryWrapper);
         for (UserMenu userMenu : userMenus) {
            UserMenuVo query = new UserMenuVo();
            BeanUtils.copyProperties(userMenu, query);
            list.add(query);
         }
         return list;
      }
      //查询所有系统功能下的所有功能
      LambdaQueryWrapper<UserMenu> query = new LambdaQueryWrapper<UserMenu>()
            .eq(UserMenu::getParentId,parentId)
            .eq(UserMenu::getAuthId,auth.getId());
      List<UserMenu> userMenuList = baseMapper.selectList(query);
      for (UserMenu userMenu : userMenuList) {
         UserMenuVo userMenuQuery = new UserMenuVo();
         //装配query
         BeanUtils.copyProperties(userMenu, userMenuQuery);
         list.add(userMenuQuery);
      }

      return list;
   }

   @Override
   public UserMenu queryMenuById(Long id) {
      return baseMapper.selectById(id);
   }

   @Override
   public List<UserMenuPrivilegeVo> queryMenuByRoleId(Long roleId) {
      List<UserMenuPrivilegeVo> queries = new ArrayList<>();

      UserLoginQuery auth = localUser.getUser();

      //菜单表
      List<UserMenuPrivilegeVo> withQueries = baseMapper.queryListMenu(auth.getId());

      //权限表
      List<PrivilegeMenuQuery> list1 = privilegeService.queryAllMenu(roleId);

      if (list1.size() == 0) {
         //没有设置权限
         for (UserMenuPrivilegeVo query : withQueries) {
            //1--没有权限
            query.setStart(1);
            queries.add(query);
         }
         return queries;
      }

      //对所有菜单摔选
      for (UserMenuPrivilegeVo query : withQueries) {

         for (PrivilegeMenuQuery menuQuery : list1) {

            if (menuQuery.getMenuId().equals(query.getId())) {
               //有此权限
               query.setStart(0);
               break;
            } else {
               //无此权限
               query.setStart(1);
            }

         }
         queries.add(query);
      }
      return queries;
   }

   @Override
   public List<UserMenuPrivilegeVo> queryMenu(Long roleId) {

      UserLoginQuery auth = localUser.getUser();

      List<UserMenuPrivilegeVo> result = new ArrayList<>();

      //查询该角色对应的权限
      List<PrivilegeMenuQuery> list = privilegeService.queryAllMenu(roleId);

      //查询所有的子菜单
      List<UserMenuPrivilegeVo> withQueries = baseMapper.queryListMenu(auth.getId());

      if (list.size() == 0) {
         //没有权限
         for (UserMenuPrivilegeVo query : withQueries) {
            UserMenuPrivilegeVo vo = new UserMenuPrivilegeVo();
            BeanUtils.copyProperties(query, vo);
            vo.setStart(1);
            result.add(vo);
         }
         return result;
      }

      //有权限
      for (UserMenuPrivilegeVo query : withQueries) {
         UserMenuPrivilegeVo vo = new UserMenuPrivilegeVo();
         BeanUtils.copyProperties(query, vo);
         for (PrivilegeMenuQuery menuQuery : list) {
            if (menuQuery.getMenuId().equals(query.getId())) {
               vo.setStart(0);
               break;
            } else {
               vo.setStart(1);
            }
         }
         result.add(vo);
      }

      return result;
   }

   @Override
   public List<UserMenuPrivilegeVo> queryParentMenu() {
      UserLoginQuery auth = localUser.getUser();

      LambdaQueryWrapper<UserMenu> wrapper = new LambdaQueryWrapper<UserMenu>()
            .eq(UserMenu::getAuthId, auth.getId())
            .eq(UserMenu::getParentId, 0);
      List<UserMenu> userMenus = baseMapper.selectList(wrapper);

      List<UserMenuPrivilegeVo> result = new ArrayList<>();
      for (UserMenu userMenu : userMenus) {
         UserMenuPrivilegeVo vo = new UserMenuPrivilegeVo();
         BeanUtils.copyProperties(userMenu, vo);
         result.add(vo);
      }
      return result;
   }
}
