package com.dkm.entity;

/**
 * @author qf
 * @date 2020/3/5
 * @vesion 1.0
 **/
public class ContinKeyVo {

   public ContinKeyVo(){};

   /**
    * 进入网关的开始时间
    */
   public static String START_TIME = "ZUUL_START_TIME";

   /**
    * 请求是否成功
    */
   public static String IS_SUCCESS = "ZUUL_IS_SUCCESS";
}
