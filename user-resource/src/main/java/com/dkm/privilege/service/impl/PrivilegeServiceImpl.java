package com.dkm.privilege.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.PrivilegeMenuQuery;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.privilege.dao.PrivilegeMapper;
import com.dkm.privilege.entity.UserMenu;
import com.dkm.privilege.entity.UserPrivilege;
import com.dkm.privilege.service.IPrivilegeService;
import com.dkm.privilege.service.IUserMenuService;
import com.dkm.type.entity.vo.AuthLoginVo;
import com.dkm.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
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
public class PrivilegeServiceImpl extends ServiceImpl<PrivilegeMapper, UserPrivilege> implements IPrivilegeService {

   @Autowired
   private LocalUser localUser;

   @Autowired
   private IdGenerator idGenerator;

   @Autowired
   private IUserMenuService userMenuService;

   @Override
   public void insertAllPrivilege(Long roleId, List<Long> menuList) {
      UserLoginQuery auth = localUser.getUser();

      //先查询数据库是否有   有就修改
      LambdaQueryWrapper<UserPrivilege> wrapper = new LambdaQueryWrapper<UserPrivilege>()
            .eq(UserPrivilege::getUserRoleId,roleId)
            .eq(UserPrivilege::getAuthId,auth.getId());
      List<UserPrivilege> selectList = baseMapper.selectList(wrapper);

      if (selectList.size() != 0) {
         //修改
         //先删再增
         LambdaQueryWrapper<UserPrivilege> lambdaQueryWrapper = new LambdaQueryWrapper<UserPrivilege>()
               .eq(UserPrivilege::getUserRoleId,roleId)
               .eq(UserPrivilege::getAuthId,auth.getId());
         int delete = baseMapper.delete(lambdaQueryWrapper);

         if (delete <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除失败");
         }
      }

      //添加
      List<UserPrivilege> list = new ArrayList<>();

      for (Long aLong : menuList) {
         UserPrivilege userPrivilege = new UserPrivilege();
         userPrivilege.setId(idGenerator.getNumberId());
         userPrivilege.setUserRoleId(roleId);
         userPrivilege.setUserMenuId(aLong);
         userPrivilege.setAuthId(auth.getId());

         list.add(userPrivilege);
      }

      int privilege = baseMapper.insertPrivilege(list);

      if (privilege <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"保存失败");
      }
   }

   @Override
   public List<PrivilegeMenuQuery> queryAllMenu(Long roleId) {
      UserLoginQuery user = localUser.getUser();
      LambdaQueryWrapper<UserPrivilege> queryWrapper = new LambdaQueryWrapper<UserPrivilege>()
            .eq(UserPrivilege::getUserRoleId,roleId)
            .eq(UserPrivilege::getAuthId,user.getId());
      //查询数据库
      List<UserPrivilege> userPrivileges = baseMapper.selectList(queryWrapper);
      //将数据装配到新集合中返回
      List<PrivilegeMenuQuery> result = new ArrayList<>();

      List<Long> list = new ArrayList<>();
      for (UserPrivilege userPrivilege : userPrivileges) {
         //将菜单Id收集然后批量查询菜单的所有数据
         list.add(userPrivilege.getUserMenuId());
      }

      List<UserMenu> userMenus = userMenuService.listAllMenu(list);

      for (UserMenu userMenu : userMenus) {
         PrivilegeMenuQuery query = new PrivilegeMenuQuery();
         query.setMenuId(userMenu.getId());
         query.setAction(userMenu.getAction());
         query.setIconClass(userMenu.getIconClass());
         query.setIconColor(userMenu.getIconColor());
         query.setMenuName(userMenu.getMenuName());
         query.setParent(userMenu.getParentId());
         result.add(query);
      }

      return result;
   }
}
