package com.dkm.authProject.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * 项目的管理基础表
 * 所有项目添加在此表操作
 * @author qf
 * @date 2020/8/10
 * @vesion 1.0
 **/
@Data
@TableName("auth_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AuthProject extends Model<AuthProject> {

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

   /**
    * 是否使用
    * 0--使用
    * 1--不使用
    */
   private Integer isStopped;
}
