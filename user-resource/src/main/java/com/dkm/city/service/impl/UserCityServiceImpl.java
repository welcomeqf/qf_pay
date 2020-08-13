package com.dkm.city.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.city.dao.UserCityMapper;
import com.dkm.city.entity.UserCity;
import com.dkm.city.entity.query.UserCityQuery;
import com.dkm.city.service.IUserCityService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
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
 * @date 2020/8/12
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCityServiceImpl extends ServiceImpl<UserCityMapper, UserCity> implements IUserCityService {

   @Autowired
   private IdGenerator idGenerator;

   @Override
   public void addOrUpdateCity(UserCity userCity) {
      if (userCity.getId() == null) {
         //添加
         if (userCity.getCityStatus() == 0) {
            LambdaQueryWrapper<UserCity> wrapper = new LambdaQueryWrapper<UserCity>()
                  .eq(UserCity::getCityName, userCity.getCityName());
            UserCity userCity1 = baseMapper.selectOne(wrapper);

            if (userCity1 != null) {
               throw new ApplicationException(CodeType.SERVICE_ERROR, "该城市已存在");
            }
         } else {
            //用户属于城市
            LambdaQueryWrapper<UserCity> wrapper = new LambdaQueryWrapper<UserCity>()
                  .eq(UserCity::getCityName, userCity.getCityName())
                  .eq(UserCity::getUserId, userCity.getUserId());
            UserCity userCity1 = baseMapper.selectOne(wrapper);

            if (userCity1 != null) {
               throw new ApplicationException(CodeType.SERVICE_ERROR, "该城市已存在");
            }
         }

         //添加城市
         userCity.setId(idGenerator.getNumberId());
         int insert = baseMapper.insert(userCity);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加城市失败");
         }
         return;
      }

      //修改城市
      UserCity city = new UserCity();
      if (userCity.getUserId() != null) {
         city.setUserId(userCity.getUserId());
      }
      city.setId(userCity.getId());
      city.setCityName(userCity.getCityName());
      city.setCityStatus(userCity.getCityStatus());

      int updateById = baseMapper.updateById(city);

      if (updateById <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改城市失败");
      }
   }

   @Override
   public void deleteCity(Long cityId) {
      int i = baseMapper.deleteById(cityId);

      if (i <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "删除城市失败");
      }
   }

   @Override
   public List<UserCityQuery> queryCityList(Long userId) {
      List<UserCityQuery> result = new ArrayList<>();
      if (userId == null) {
         //查询系统城市
         LambdaQueryWrapper<UserCity> wrapper = new LambdaQueryWrapper<UserCity>()
               .eq(UserCity::getCityStatus, 0);
         List<UserCity> list = baseMapper.selectList(wrapper);

         for (UserCity city : list) {
            UserCityQuery query = new UserCityQuery();
            BeanUtils.copyProperties(city, query);
            result.add(query);
         }
      } else {
         //查询用户所属城市
         LambdaQueryWrapper<UserCity> wrapper = new LambdaQueryWrapper<UserCity>()
               .eq(UserCity::getUserId, userId);
         List<UserCity> list = baseMapper.selectList(wrapper);

         for (UserCity city : list) {
            UserCityQuery query = new UserCityQuery();
            BeanUtils.copyProperties(city, query);
            result.add(query);
         }
      }
      return result;
   }
}
