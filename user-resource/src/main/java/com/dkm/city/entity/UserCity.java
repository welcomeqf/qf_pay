package com.dkm.city.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Data
@TableName("city_base")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserCity extends Model<UserCity> {

   /**
    * 城市Id
    */
   private Long id;

   /**
    * 城市名称
    */
   private String cityName;

   /**
    * 用户id
    */
   private Long userId;

   /**
    * 0--系统城市 1--用户的属于城市
    */
   private Integer cityStatus;
}
