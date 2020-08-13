package com.dkm.city.entity.query;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/12
 * @vesion 1.0
 **/
@Data
public class UserCityQuery {

   /**
    * 城市Id
    */
   private Long id;

   /**
    * 城市名称
    */
   private String cityName;
}
