package com.dkm.type.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Data
public class AuthLoginQueryResultVo {

   /**
    * 设备id
    */
   private Long authProjectId;

   /**
    * 项目名称
    */
   private String authName;

   /**
    *  登录方式的集合
    */
   private List<QueryLoginTypeVo> queryLoginTypeVoList;
}
