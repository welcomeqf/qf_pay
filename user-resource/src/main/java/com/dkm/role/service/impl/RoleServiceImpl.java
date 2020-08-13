package com.dkm.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.role.dao.RoleMapper;
import com.dkm.role.entity.Role;
import com.dkm.role.entity.bo.RoleAddBO;
import com.dkm.role.entity.vo.RoleVo;
import com.dkm.role.service.IRoleService;
import com.dkm.utils.IdGenerator;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

   @Autowired
   private LocalUser localUser;

   @Autowired
   private IdGenerator idGenerator;

   @Override
   public synchronized void addOrUpdateRole(RoleAddBO roleAddBO) {
      UserLoginQuery user = localUser.getUser();
      if (roleAddBO.getId() == null) {
         //添加角色
         LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
               .eq(Role::getAuthId, user.getId())
               .eq(Role::getRoleName, roleAddBO.getRoleName());
         Role role = baseMapper.selectOne(wrapper);

         if (role != null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该角色名已被添加");
         }

         //添加角色
         Role userRole = new Role();
         userRole.setId(idGenerator.getNumberId());
         userRole.setAuthId(user.getId());
         userRole.setIsStopped(0);
         userRole.setRoleName(roleAddBO.getRoleName());
         userRole.setRoleStatus(roleAddBO.getRoleStatus());
         int insert = baseMapper.insert(userRole);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加角色失败");
         }
      } else {
         //修改角色
         Role userRole = new Role();
         userRole.setId(roleAddBO.getId());
         userRole.setRoleName(roleAddBO.getRoleName());
         userRole.setRoleStatus(roleAddBO.getRoleStatus());

         int updateById = baseMapper.updateById(userRole);

         if (updateById <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改角色失败");
         }
      }
   }

   @Override
   public Role queryOneById(Long roleId) {
      return baseMapper.selectById(roleId);
   }

   @Override
   public void deleteRole(Long roleId) {
      int i = baseMapper.deleteById(roleId);

      if (i <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "删除角色失败");
      }
   }

   @Override
   public List<RoleVo> listAllRole() {
      UserLoginQuery user = localUser.getUser();
      LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
            .eq(Role::getAuthId, user.getId());
      List<Role> roles = baseMapper.selectList(wrapper);
      List<RoleVo> result = new ArrayList<>();
      for (Role role : roles) {
         RoleVo vo = new RoleVo();
         BeanUtils.copyProperties(role, vo);
         result.add(vo);
      }
      return result;
   }

   @Override
   public void updateRoleStatus(Long roleId, Integer status) {
      Role role = new Role();
      role.setId(roleId);
      role.setIsStopped(status);
      int updateById = baseMapper.updateById(role);

      if (updateById <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改角色状态失败");
      }
   }

   @Override
   public List<RoleVo> listToUserRole() {
      UserLoginQuery user = localUser.getUser();
      LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
            .eq(Role::getAuthId, user.getId())
            .eq(Role::getIsStopped, 0);
      List<Role> roles = baseMapper.selectList(wrapper);
      List<RoleVo> result = new ArrayList<>();
      for (Role role : roles) {
         RoleVo vo = new RoleVo();
         BeanUtils.copyProperties(role, vo);
         result.add(vo);
      }
      return result;
   }
}
