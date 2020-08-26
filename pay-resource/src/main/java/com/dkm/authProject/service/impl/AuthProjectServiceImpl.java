package com.dkm.authProject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.authProject.dao.AuthProjectMapper;
import com.dkm.authProject.entity.AuthProject;
import com.dkm.authProject.entity.bo.AuthProjectBO;
import com.dkm.authProject.service.IAuthProjectService;
import com.dkm.authUser.service.IAuthService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.utils.IdGenerator;
import com.dkm.utils.StringUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/10
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthProjectServiceImpl extends ServiceImpl<AuthProjectMapper, AuthProject> implements IAuthProjectService {

   @Autowired
   private IdGenerator idGenerator;

   @Autowired
   private IAuthService authService;

   @Override
   public void addOrUpdateProject(AuthProjectBO authProjectBO) {
      AuthProject authProject = new AuthProject();
      if (authProjectBO.getId() == null) {
         //增加
         BeanUtils.copyProperties(authProjectBO,authProject);
         authProject.setId(idGenerator.getNumberId());
         authProject.setIsStopped(0);
         int insert = baseMapper.insert(authProject);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "增加失败");
         }
      } else {
         //修改
         authProject.setId(authProjectBO.getId());
         if (authProjectBO.getAuthStatus() != null) {
            authProject.setAuthStatus(authProjectBO.getAuthStatus());
         }
         if (StringUtils.isNotBlank(authProjectBO.getAuthName())) {
            authProject.setAuthName(authProjectBO.getAuthName());
         }
         if (StringUtils.isNotBlank(authProjectBO.getAuthDescribe())) {
            authProject.setAuthDescribe(authProjectBO.getAuthDescribe());
         }

         int updateById = baseMapper.updateById(authProject);

         if (updateById <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
         }
      }
   }

   @Override
   public List<AuthProject> listAllProject() {
      return baseMapper.selectList(null);
   }

   @Override
   public List<AuthProject> listToUserProject() {
      LambdaQueryWrapper<AuthProject> wrapper = new LambdaQueryWrapper<AuthProject>()
            .eq(AuthProject::getIsStopped, 0);
      return baseMapper.selectList(wrapper);
   }

   @Override
   public void updateStatus(Long projectId, Integer status) {
      AuthProject authProject = new AuthProject();
      authProject.setId(projectId);
      authProject.setIsStopped(status);

      int updateById = baseMapper.updateById(authProject);

      if (updateById <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
      }

      //同时修改设备的状态
      authService.updateToStopAuth(projectId, status);
   }
}
