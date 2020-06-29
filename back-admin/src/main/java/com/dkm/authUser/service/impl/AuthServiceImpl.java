package com.dkm.authUser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.authUser.dao.AuthMapper;
import com.dkm.authUser.entity.AuthInfo;
import com.dkm.authUser.entity.vo.AuthLoginVo;
import com.dkm.authUser.entity.vo.AuthRegisterVo;
import com.dkm.authUser.service.IAuthService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.utils.IdGenerator;
import com.dkm.utils.ShaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author qf
 * @date 2020/3/16
 * @vesion 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl extends ServiceImpl<AuthMapper, AuthInfo> implements IAuthService {

   @Autowired
   private IdGenerator idGenerator;

   /**
    * 注册设备
    * @param vo
    * @return
    */
   @Override
   public AuthLoginVo registerAuth(AuthRegisterVo vo) {

      Long authId = null;

      AuthInfo authInfo = new AuthInfo();
      authInfo.setAuthName(vo.getAuthName());
      authInfo.setAuthDescribe(vo.getAuthDescribe());
      authInfo.setAppId(vo.getAppId());
      authInfo.setCreateDate(LocalDateTime.now());
      authInfo.setUpdateDate(LocalDateTime.now());
      authInfo.setIsStopped(vo.getIsStopped());

      authInfo.setWxAppId(vo.getWxAppId());
      authInfo.setMchId(vo.getMchId());
      authInfo.setPaterNerKey(vo.getPaterNerKey());

      if (vo.getId() == null) {
         String key = idGenerator.getOrderCode();
         String secret = idGenerator.getUuid();
         authId = idGenerator.getNumberId();
         authInfo.setId(authId);
         authInfo.setAuthUser(key);
         authInfo.setIsStopped(0);
         authInfo.setAuthPassword(ShaUtils.getSha1(secret));
         //注册设备
         int insert = baseMapper.insert(authInfo);

         if (insert <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加设备失败");
         }
         AuthLoginVo result = new AuthLoginVo();
         result.setAuthUserKey(key);
         result.setAuthPassword(secret);

         return result;
      } else {
         authId = vo.getId();
         authInfo.setId(authId);
         //修改设备
         int update = baseMapper.updateById(authInfo);

         if (update <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改设备失败");
         }
         return null;
      }

   }


   /**
    * 设备登录
    * @param vo
    * @return
    */
   @Override
   public UserLoginQuery authLogin(AuthLoginVo vo) {
      String password = ShaUtils.getSha1(vo.getAuthPassword());

      LambdaQueryWrapper<AuthInfo> wrapper = new LambdaQueryWrapper<AuthInfo>()
            .eq(AuthInfo::getAuthUser,vo.getAuthUserKey())
            .eq(AuthInfo::getAuthPassword,password);

      AuthInfo authInfo = baseMapper.selectOne(wrapper);

      if (authInfo == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "用户名或密码错误");
      }

      if (authInfo.getIsStopped() == 1) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "该设备已停用");
      }

      UserLoginQuery query = new UserLoginQuery();
      query.setId(authInfo.getId());
      return query;
   }

   /**
    * 查询一条记录
    * @param id
    * @return
    */
   @Override
   public AuthInfo queryAuthOne(Long id) {
      return baseMapper.selectById(id);
   }


   /**
    * 查询所有设备
    * @return
    */
   @Override
   public List<AuthInfo> listAuth() {
      return baseMapper.selectList(null);
   }
}
