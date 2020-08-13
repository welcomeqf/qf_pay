package com.dkm.type.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Data
public class AuthLoginBO {

   /**
    * 设备id
    */
   private Long authProjectId;

   /**
    * 登录方式id的集合
    */
   private List<Long> list;
}
