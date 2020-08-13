package com.dkm.type.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.type.dao.UserLoginTypeMapper;
import com.dkm.type.entity.UserLoginType;
import com.dkm.type.entity.bo.UserLoginTypeBO;
import com.dkm.type.service.IUserLoginTypeService;
import com.dkm.utils.IdGenerator;
import com.dkm.utils.StringUtils;
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
public class UserLoginTypeServiceImpl extends ServiceImpl<UserLoginTypeMapper, UserLoginType> implements IUserLoginTypeService {

   @Autowired
   private IdGenerator idGenerator;

   @Override
   public void addOrUpdateLoginType(UserLoginTypeBO userLoginTypeBO) {
      UserLoginType userLoginType = new UserLoginType();
      if (userLoginTypeBO.getId() == null) {
         //增加
         BeanUtils.copyProperties(userLoginTypeBO, userLoginType);
         userLoginType.setId(idGenerator.getNumberId());
         userLoginType.setLoginStatus(0);

         int insert = baseMapper.insert(userLoginType);

         if (insert <= 0) {
            log.info("添加登录方式失败");
            throw new ApplicationException(CodeType.SERVICE_ERROR);
         }
      } else {
         //修改
         userLoginType.setId(userLoginTypeBO.getId());
         if (StringUtils.isNotBlank(userLoginTypeBO.getLoginName())) {
            userLoginType.setLoginName(userLoginTypeBO.getLoginName());
         }
         if (userLoginTypeBO.getLoginType() != null) {
            userLoginType.setLoginType(userLoginTypeBO.getLoginType());
         }

         int updateById = baseMapper.updateById(userLoginType);

         if (updateById <= 0) {
            log.info("修改登录方式失败");
            throw new ApplicationException(CodeType.SERVICE_ERROR);
         }
      }
   }

   @Override
   public List<UserLoginType> listAllLoginType() {
      return baseMapper.selectList(null);
   }

   @Override
   public List<UserLoginType> listToUserLoginType() {
      LambdaQueryWrapper<UserLoginType> wrapper = new LambdaQueryWrapper<UserLoginType>()
            .eq(UserLoginType::getLoginStatus, 0);
      return baseMapper.selectList(wrapper);
   }

   @Override
   public void updateLoginTypeStatus(Long loginTypeId, Integer status) {
      UserLoginType userLoginType = new UserLoginType();
      userLoginType.setId(loginTypeId);
      userLoginType.setLoginStatus(status);

      int updateById = baseMapper.updateById(userLoginType);

      if (updateById <= 0) {
         log.info("登录方式->启用或停用失败");
         throw new ApplicationException(CodeType.SERVICE_ERROR);
      }
   }
}
