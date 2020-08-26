package com.dkm.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.user.dao.UserMapper;
import com.dkm.user.entity.User;
import com.dkm.user.entity.bo.UserBO;
import com.dkm.user.entity.bo.UserInfoBO;
import com.dkm.user.entity.bo.UserLoginBO;
import com.dkm.user.entity.bo.WeChatUtilBO;
import com.dkm.user.entity.vo.UserResultVo;
import com.dkm.user.entity.vo.UserVo;
import com.dkm.user.service.IUserService;
import com.dkm.user.utils.WeChatUtil;
import com.dkm.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2020/8/13
 * @vesion 1.0
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

   @Autowired
   private IdGenerator idGenerator;

   @Autowired
   private LocalUser localUser;

   @Autowired
   private WeChatUtil weChatUtil;

   @Override
   public void addUser(UserBO userBO) {
      UserLoginQuery auth = localUser.getUser();
      //先查询
      LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
            .eq(User::getAccount, userBO.getAccount())
            .eq(User::getAuthId, auth.getId());

      User selectOne = baseMapper.selectOne(wrapper);

      if (selectOne != null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "该账号已被注册");
      }

      User user = new User();

      user.setId(idGenerator.getNumberId());

      //得到加密后的密码
      String password = ShaUtils.getSha1(userBO.getPassword());

      user.setAccount(userBO.getAccount());
      user.setPassword(password);
      user.setAuthId(auth.getId());
      if (userBO.getUserRoleId() != null) {
         user.setUserRoleId(userBO.getUserRoleId());
      }
      if (StringUtils.isNotBlank(userBO.getCName())) {
         user.setCName(userBO.getCName());
      }
      if (StringUtils.isNotBlank(userBO.getUserCard())) {
         user.setUserCard(userBO.getUserCard());
      }
      if (userBO.getCompanyId() != null) {
         user.setCompanyId(userBO.getCompanyId());
      }
     if (userBO.getUserSex() != null) {
        user.setUserSex(userBO.getUserSex());
     }
     if (userBO.getUserAge() != null) {
        user.setUserAge(userBO.getUserAge());
     }
     if (userBO.getIsStopped() != null) {
        user.setIsStopped(userBO.getIsStopped());
     } else {
        user.setIsStopped(0);
     }
     if (userBO.getUserStatus() == null) {
        user.setUserStatus(0);
     } else {
        user.setUserStatus(userBO.getUserStatus());
     }

     user.setCreateDate(LocalDateTime.now());
     int insert = baseMapper.insert(user);

     if (insert <= 0) {
        throw new ApplicationException(CodeType.SERVICE_ERROR, "注册失败");
     }
   }

   @Override
   public UserResultVo loginUser(UserLoginBO bo) {
      UserLoginQuery auth = localUser.getUser();

      LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
            .eq(User::getAccount,bo.getUserName())
            .eq(User::getAuthId,auth.getId());
      User user = baseMapper.selectOne(queryWrapper);

      if (user == null) {
         log.error("user login fail.");
         throw new ApplicationException(CodeType.SERVICE_ERROR,"用户名有误");
      }

      if (!ShaUtils.getSha1(bo.getPassword()).equals(user.getPassword())) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"密码错误");
      }

      if (user.getIsStopped() == 1) {
         throw new ApplicationException(CodeType.SERVICE_ERROR,"该账号还未启用");
      }

      //生成token返回
      UserResultVo vo = new UserResultVo();
      UserLoginQuery query = new UserLoginQuery();
      query.setId(user.getId());
      //24小时
      String token = JwtUtil.createJwt(1000 * 60 * 60 * 24, query);
      vo.setToken(token);
      LocalDateTime time = LocalDateTime.now().plusDays(1);
      String date = DateUtil.formatDateTime(time);
      vo.setExpTime(date);
      return vo;
   }

   @Override
   public void perfectUser(UserInfoBO bo) {
      User user = new User();
      user.setId(bo.getId());
      if (bo.getUserRoleId() != null) {
         user.setUserRoleId(bo.getUserRoleId());
      }
      if (bo.getCompanyId() != null) {
         user.setCompanyId(bo.getCompanyId());
      }
      if (StringUtils.isNotBlank(bo.getCName())) {
         user.setCName(bo.getCName());
      }
      if (StringUtils.isNotBlank(bo.getEName())) {
         user.setEName(bo.getEName());
      }
      if (bo.getUserSex() != null) {
         user.setUserSex(bo.getUserSex());
      }
      if (bo.getUserAge() != null) {
         user.setUserAge(bo.getUserAge());
      }
      if (StringUtils.isNotBlank(bo.getUserCard())) {
         user.setUserCard(bo.getUserCard());
      }
      if (StringUtils.isNotBlank(bo.getUserImgUrl())) {
         user.setUserImgUrl(bo.getUserImgUrl());
      }
      if (StringUtils.isNotBlank(bo.getUserTel())) {
         user.setUserTel(bo.getUserTel());
      }
      if (bo.getIsStopped() != null) {
         user.setIsStopped(bo.getIsStopped());
      }
      if (bo.getUserStatus() != null) {
         user.setUserStatus(bo.getUserStatus());
      }

      int updateById = baseMapper.updateById(user);

      if (updateById <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
      }
   }

   @Override
   public UserResultVo weChatLoginUserInfo(String code) {

      //定义用户id生成token
      Long userId;
      try {
         WeChatUtilBO weChatUtilBO = weChatUtil.codeToUserInfo(code);

         //查询用户
         LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
               .eq(User::getWeChatOpenId, weChatUtilBO.getWeChatOpenId());

         User user = baseMapper.selectOne(wrapper);

         if (user == null) {
            //不存在，就添加
            User user1 = new User();
            userId = idGenerator.getNumberId();
            user1.setId(userId);
            user1.setWeChatOpenId(weChatUtilBO.getWeChatOpenId());
            user1.setCName(weChatUtilBO.getWeChatNickName());
            if ("1".equals(weChatUtilBO.getWeChatSex())) {
               //男
               user1.setUserSex(0);
            } else {
               //女
               user1.setUserSex(1);
            }
            user1.setRemark(weChatUtilBO.getWeChatCountry() + weChatUtilBO.getWeChatProvince() + weChatUtilBO.getWeChatCity());
            user1.setUserImgUrl(weChatUtilBO.getWeChatHeadImgUrl());
            user1.setCreateDate(LocalDateTime.now());
            int insert = baseMapper.insert(user1);

            if (insert <= 0) {
               throw new ApplicationException(CodeType.SERVICE_ERROR, "微信授权失败");
            }
         } else {
            //已有账号  就修改信息  授权登录
            User user1 = new User();
            userId = user.getId();
            user1.setId(userId);
            user1.setCName(weChatUtilBO.getWeChatNickName());
            if ("1".equals(weChatUtilBO.getWeChatSex())) {
               //男
               user1.setUserSex(0);
            } else {
               //女
               user1.setUserSex(1);
            }
            user1.setRemark(weChatUtilBO.getWeChatCountry() + weChatUtilBO.getWeChatProvince() + weChatUtilBO.getWeChatCity());
            user1.setUserImgUrl(weChatUtilBO.getWeChatHeadImgUrl());

            int updateById = baseMapper.updateById(user1);

            if (updateById <= 0) {
               throw new ApplicationException(CodeType.SERVICE_ERROR, "微信授权失败，请重新登录");
            }
         }

      } catch (IOException e) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "网路请求失败");
      }
      //生成token返回
      UserResultVo vo = new UserResultVo();
      UserLoginQuery query = new UserLoginQuery();
      query.setId(userId);
      //24小时
      String token = JwtUtil.createJwt(1000 * 60 * 60 * 24, query);
      vo.setToken(token);
      LocalDateTime time = LocalDateTime.now().plusDays(1);
      String date = DateUtil.formatDateTime(time);
      vo.setExpTime(date);
      return vo;
   }

   @Override
   public UserVo queryOne(Long userId) {
      User user = baseMapper.selectById(userId);
      UserVo vo = new UserVo();
      BeanUtils.copyProperties(user, vo);
      return vo;
   }

   @Override
   public List<UserVo> listUserByRole(Long roleId) {
      UserLoginQuery auth = localUser.getUser();
      LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
            .eq(User::getAuthId, auth.getId())
            .eq(User::getUserRoleId, roleId);
      List<User> list = baseMapper.selectList(wrapper);

      List<UserVo> result = new ArrayList<>();
      for (User user : list) {
         UserVo vo = new UserVo();
         BeanUtils.copyProperties(user, vo);
         result.add(vo);
      }
      return result;
   }

}
