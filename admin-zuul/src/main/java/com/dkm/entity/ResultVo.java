package com.dkm.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/3/5
 * @vesion 1.0
 **/
@Data
public class ResultVo<T> implements Serializable {

   private Integer code;

   private String msg;

   private T data;

   private LocalDateTime nowDate;

   public static ResultVo fall (String msg) {
      ResultVo vo = new ResultVo();
      vo.setCode(999);
      vo.setMsg(msg);
      LocalDateTime now = LocalDateTime.now();
      vo.setNowDate(now);
      return vo;
   }
}
