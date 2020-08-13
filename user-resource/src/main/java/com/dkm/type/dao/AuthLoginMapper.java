package com.dkm.type.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.type.entity.AuthLogin;
import com.dkm.type.entity.bo.AuthLoginInsertBO;
import com.dkm.type.entity.vo.AuthLoginVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/10
 * @vesion 1.0
 **/
@Component
public interface AuthLoginMapper extends IBaseMapper<AuthLogin> {

   /**
    *  批量增加工程设备以及对应的登录方式
    * @param list 批量增加的参数
    * @return 返回添加以后的结果
    */
   Integer addAllAuthLogin (List<AuthLoginInsertBO> list);

   /**
    *  返回所有工程设备以及对应的所有的登录方式
    * @return 返回结果
    */
   List<AuthLoginVo> listAuthLogin();
}
