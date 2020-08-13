package com.dkm.privilege.dao;

import com.dkm.IBaseMapper.IBaseMapper;
import com.dkm.privilege.entity.UserMenu;
import com.dkm.privilege.entity.vo.UserMenuPrivilegeVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Component
public interface UserMenuMapper extends IBaseMapper<UserMenu> {

   /**
    *    返回所有菜单
    * @param id 设备Id
    * @return 返回结果
    */
   List<UserMenuPrivilegeVo> queryListMenu(Long id);
}
