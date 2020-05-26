package com.dkm.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkm.authUser.entity.AuthInfo;
import com.dkm.authUser.service.IAuthService;
import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.dkm.jwt.contain.LocalUser;
import com.dkm.jwt.entity.UserLoginQuery;
import com.dkm.pay.dao.PayInfoMapper;
import com.dkm.pay.entity.PayInfo;
import com.dkm.pay.entity.vo.PayParamVo;
import com.dkm.pay.entity.vo.PayReturnVo;
import com.dkm.pay.entity.wxBo.WxInsertBo;
import com.dkm.pay.entity.wxBo.WxRefundBo;
import com.dkm.pay.entity.wxBo.WxResultBo;
import com.dkm.pay.service.IPayInfoService;
import com.dkm.utils.IdGenerator;
import com.dkm.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author qf
 * @date 2020/3/17
 * @vesion 1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class PayInfoServiceImpl extends ServiceImpl<PayInfoMapper, PayInfo> implements IPayInfoService {

   @Autowired
   private IdGenerator idGenerator;

   @Autowired
   private LocalUser localUser;

   @Autowired
   private IAuthService authService;

   /**
    * 添加一条支付宝支付记录
    * @param vo
    */
   @Override
   public String insertPayInfo(PayReturnVo vo) {

      LambdaQueryWrapper<PayInfo> wrapper = new LambdaQueryWrapper<PayInfo>()
            .eq(PayInfo::getOrderNo, vo.getOrderNo());

      PayInfo info = baseMapper.selectOne(wrapper);

      UserLoginQuery user = localUser.getUser("user");

      PayInfo payInfo = new PayInfo();

//      payInfo.setAuthId(689146467461533696L);

      payInfo.setAuthId(user.getId());

      //查询设备表中的appId
      AuthInfo authInfo = authService.queryAuthOne(user.getId());

      if (authInfo == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "查询设备失败");
      }

      if (info != null) {
         return authInfo.getAppId();
      }


      payInfo.setId(idGenerator.getNumberId());



      LocalDateTime now = LocalDateTime.now();
      payInfo.setCreateDate(now);
      payInfo.setPayBody(vo.getBody());
      payInfo.setPayMoney(vo.getPrice());
      payInfo.setPaySubject(vo.getSubject());
      payInfo.setReturnUrl(vo.getReturnUrl());
      payInfo.setUpdateDate(now);
      payInfo.setOrderNo(vo.getOrderNo());
      payInfo.setPayType(0);

      int insert = baseMapper.insert(payInfo);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR);
      }

      if (StringUtils.isBlank(authInfo.getAppId())) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "请先配置支付宝的AppId");
      }

      //返回appId
      return authInfo.getAppId();

   }

   /**
    * 添加微信支付的记录
    * @param bo
    * @return
    */
   @Override
   public WxResultBo insertWxPayInfo(WxInsertBo bo) {

      LambdaQueryWrapper<PayInfo> wrapper = new LambdaQueryWrapper<PayInfo>()
            .eq(PayInfo::getOrderNo, bo.getOrderNo());

      PayInfo info = baseMapper.selectOne(wrapper);

      UserLoginQuery user = localUser.getUser("user");

      //查询设备表中的appId
      AuthInfo authInfo = authService.queryAuthOne(user.getId());

      if (authInfo == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "查询设备失败");
      }

      WxResultBo result = new WxResultBo();
      //查询是否已添加了此订单
      //有该订单了就不再添加
      if (info != null) {
         result.setWxAppId(authInfo.getWxAppId());
         result.setMchId(authInfo.getMchId());
         result.setPaterNerKey(authInfo.getPaterNerKey());
         return result;
      }


      PayInfo payInfo = new PayInfo();

//      payInfo.setAuthId(689146467461533696L);

      payInfo.setAuthId(user.getId());


      payInfo.setId(idGenerator.getNumberId());

      LocalDateTime now = LocalDateTime.now();
      payInfo.setCreateDate(now);
      payInfo.setPayBody(bo.getBody());
      payInfo.setPayMoney(bo.getPrice());
      payInfo.setReturnUrl(bo.getNotifyUrl());
      payInfo.setUpdateDate(now);
      payInfo.setOrderNo(bo.getOrderNo());
      payInfo.setPayType(1);

      int insert = baseMapper.insert(payInfo);

      if (insert <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR);
      }

      //返回微信支付需要的信息
      result.setWxAppId(authInfo.getWxAppId());
      result.setMchId(authInfo.getMchId());
      result.setPaterNerKey(authInfo.getPaterNerKey());
      return result;
   }


   /**
    * 修改支付状态
    * @param vo
    */
   @Override
   public String updateAliPayStatus(PayParamVo vo) {

      PayInfo payInfo = new PayInfo();
      payInfo.setIsPaySuccess(vo.getIsPaySuccess());
      payInfo.setPayTime(LocalDateTime.now());
      payInfo.setUpdateDate(LocalDateTime.now());
      payInfo.setPayType(vo.getPayType());
      payInfo.setPayNo(vo.getPayNo());

      LambdaQueryWrapper<PayInfo> wrapper = new LambdaQueryWrapper<PayInfo>()
            .eq(PayInfo::getOrderNo,vo.getOrderNo());

      PayInfo info = baseMapper.selectOne(wrapper);

      int update = baseMapper.update(payInfo, wrapper);

      if (update <= 0) {
         throw new ApplicationException(CodeType.SERVICE_ERROR);
      }

      return info.getReturnUrl();

   }



   @Override
   public String queryAppId() {

      UserLoginQuery user = localUser.getUser("user");

      AuthInfo authInfo = authService.queryAuthOne(user.getId());

      if (authInfo == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "分配的设备账号有误");
      }

      return authInfo.getAppId();
   }

   /**
    * 查询微信支付相关信息
    * @return
    */
   @Override
   public WxRefundBo queryWxAppId() {

      UserLoginQuery user = localUser.getUser("user");

      AuthInfo authInfo = authService.queryAuthOne(user.getId());

      if (authInfo == null) {
         throw new ApplicationException(CodeType.SERVICE_ERROR, "分配的设备账号有误");
      }

      WxRefundBo bo = new WxRefundBo();
      bo.setWxAppId(authInfo.getWxAppId());
      bo.setMchId(authInfo.getMchId());
      bo.setPaterNerKey(authInfo.getPaterNerKey());
      return bo;
   }


   @Override
   public PayInfo queryOne(String orderNo) {
      LambdaQueryWrapper<PayInfo> wrapper = new LambdaQueryWrapper<PayInfo>()
            .eq(PayInfo::getOrderNo,orderNo);

      return baseMapper.selectOne(wrapper);
   }
}
