package com.dkm.authProject.entity.bo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/8/10
 * @vesion 1.0
 **/
@Data
public class AuthProjectBO {
   /**
    * 主键id
    */
   private Long id;

   /**
    * 项目名称
    */
   private String authName;

   /**
    * 设备描述
    */
   private String authDescribe;

   /**
    * 0--展示支付填写信息 1--不展示支付填写信息
    * 用来给前端做判断
    */
   private Integer authStatus;
}
