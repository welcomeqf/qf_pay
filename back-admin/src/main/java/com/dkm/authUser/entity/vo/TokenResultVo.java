package com.dkm.authUser.entity.vo;

import lombok.Data;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Data
public class TokenResultVo {

   private String token;

   private String exp;
}
