package com.dkm.city.service;

import com.dkm.city.entity.UserCity;
import com.dkm.city.entity.query.UserCityQuery;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/12
 * @vesion 1.0
 **/
public interface IUserCityService {

   /**
    * 添加或者修改城市
    * 添加城市分系统城市和用户城市
    * @param userCity 参数 根据id决定是添加还是修改
    */
    void addOrUpdateCity (UserCity userCity);

   /**
    *  根据城市id删除城市
    * @param cityId 城市Id
    */
    void deleteCity (Long cityId);

   /**
    *  查询城市
    * @param userId null--系统城市 传值--用户的属于城市
    * @return 根据条件返回所需要的城市列表
    */
    List<UserCityQuery> queryCityList (Long userId);
}
