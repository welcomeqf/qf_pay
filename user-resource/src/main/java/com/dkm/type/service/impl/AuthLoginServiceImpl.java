package com.dkm.type.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.type.dao.AuthLoginMapper;
import com.dkm.type.entity.AuthLogin;
import com.dkm.type.entity.bo.AuthLoginBO;
import com.dkm.type.entity.bo.AuthLoginInsertBO;
import com.dkm.type.entity.vo.AuthLoginQueryResultVo;
import com.dkm.type.entity.vo.AuthLoginVo;
import com.dkm.type.entity.vo.QueryLoginTypeVo;
import com.dkm.type.service.IAuthLoginService;
import com.dkm.utils.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author qf
 * @date 2020/8/11
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthLoginServiceImpl extends ServiceImpl<AuthLoginMapper, AuthLogin> implements IAuthLoginService {

   @Autowired
   private IdGenerator idGenerator;

   @Override
   public void addAllAuthLogin(AuthLoginBO bo) {
      //批量增加登录方式
      List<AuthLoginInsertBO> list = new ArrayList<>();

      for (Long aLong : bo.getList()) {
         AuthLoginInsertBO loginInsertBO = new AuthLoginInsertBO();
         loginInsertBO.setId(idGenerator.getNumberId());
         loginInsertBO.setAuthProjectId(bo.getAuthProjectId());
         loginInsertBO.setLoginId(aLong);
         list.add(loginInsertBO);
      }

      Integer integer = baseMapper.addAllAuthLogin(list);

      if (integer <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "添加失败");
      }
   }

   @Override
   public List<AuthLoginQueryResultVo> listAuthLogin() {
      List<AuthLoginVo> list = baseMapper.listAuthLogin();

      List<AuthLoginQueryResultVo> result = new ArrayList<>();
      Set<Long> set = new HashSet<>();
      for (AuthLoginVo authLoginVo : list) {
         set.add(authLoginVo.getAuthProjectId());
      }

      for (Long aLong : set) {
         AuthLoginQueryResultVo resultVo = new AuthLoginQueryResultVo();
         resultVo.setAuthProjectId(aLong);
         List<QueryLoginTypeVo> queryLoginTypeVoList = new ArrayList<>();
         for (AuthLoginVo authLoginVo : list) {
            if (aLong.equals(authLoginVo.getAuthProjectId())) {
               QueryLoginTypeVo vo = new QueryLoginTypeVo();
               vo.setLoginId(authLoginVo.getLoginId());
               vo.setLoginName(authLoginVo.getLoginName());
               vo.setLoginType(authLoginVo.getLoginType());
               queryLoginTypeVoList.add(vo);
               resultVo.setAuthName(authLoginVo.getAuthName());
            }
         }

         resultVo.setQueryLoginTypeVoList(queryLoginTypeVoList);
         result.add(resultVo);
      }

      return result;
   }

   @Override
   public void deleteAuthLogin(Long authProjectId, Long loginId) {
      LambdaQueryWrapper<AuthLogin> wrapper = new LambdaQueryWrapper<AuthLogin>()
            .eq(AuthLogin::getAuthProjectId, authProjectId)
            .eq(AuthLogin::getLoginId, loginId);

      int delete = baseMapper.delete(wrapper);

      if (delete <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "删除失败");
      }
   }
}
