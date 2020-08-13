package com.dkm.privilege.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.privilege.entity.UserPrivilege;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Component
public interface PrivilegeMapper extends IBaseMapper<UserPrivilege> {

   /**
    *  批量增加权限
    * @param list 权限的参数列表集合
    * @return 返回添加结果
    */
   Integer insertPrivilege (List<UserPrivilege> list);
}
