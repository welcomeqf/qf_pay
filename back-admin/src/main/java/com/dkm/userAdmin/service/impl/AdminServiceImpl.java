package com.dkm.userAdmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.userAdmin.dao.AdminMapper;
import com.dkm.userAdmin.entity.UserAdmin;
import com.dkm.userAdmin.entity.vo.LoginVo;
import com.dkm.userAdmin.service.IAdminService;
import com.dkm.utils.IdGenerator;
import com.dkm.utils.ShaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qf
 * @date 2020/3/25
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl extends ServiceImpl<AdminMapper, UserAdmin> implements IAdminService {

   @Autowired
   private IdGenerator idGenerator;

   @Override
   public LoginVo addAdmin(String userName, String password) {

      LambdaQueryWrapper<UserAdmin> wrapper = new LambdaQueryWrapper<UserAdmin>()
            .eq(UserAdmin::getUserName,userName);

      UserAdmin userAdmin = baseMapper.selectOne(wrapper);

      if (userAdmin != null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "该账号已被注册");
      }

      UserAdmin admin = new UserAdmin();
      admin.setId(idGenerator.getNumberId());
      admin.setUserName(userName);
      admin.setStatus(0);
      admin.setUserPassword(ShaUtils.getSha1(password));

      int insert = baseMapper.insert(admin);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "添加db失败");
      }

      LoginVo vo = new LoginVo();

      vo.setUserName(userName);
      vo.setUserPassword(password);

      return vo;
   }

   @Override
   public void loginAdmin(String userName, String password) {

      LambdaQueryWrapper<UserAdmin> wrapper = new LambdaQueryWrapper<UserAdmin>()
            .eq(UserAdmin::getUserName,userName)
            .eq(UserAdmin::getUserPassword,ShaUtils.getSha1(password));

      UserAdmin admin = baseMapper.selectOne(wrapper);

      if (admin == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "用户名或者密码错误");
      }

      if (admin.getStatus() == 1) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "该账号已被冻结");
      }

   }
}
