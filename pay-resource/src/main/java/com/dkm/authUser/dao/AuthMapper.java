package com.dkm.authUser.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.authUser.entity.AuthInfo;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Component
public interface AuthMapper extends IBaseMapper<AuthInfo> {
}
