package com.dkm.pay.service;

import com.dkm.pay.entity.PayInfo;
import com.dkm.pay.entity.vo.PayParamVo;
import com.dkm.pay.entity.vo.PayReturnVo;
import com.dkm.pay.entity.wxBo.WxInsertBo;
import com.dkm.pay.entity.wxBo.WxRefundBo;
import com.dkm.pay.entity.wxBo.WxResultBo;

/**
 * @author qf
 * @date 2020/3/17
 * @vesion 1.0
 **/
public interface IPayInfoService {


   /**
    * 添加一条支付宝支付记录
    * 返回appId
    * @param vo
    * @return
    */
   String insertPayInfo (PayReturnVo vo);

   /**
    * 添加一条微信支付的记录
    * @param bo
    * @return
    */
   WxResultBo insertWxPayInfo (WxInsertBo bo);

   /**
    * 修改支付状态
    * @param vo
    * @return
    */
   String updateAliPayStatus (PayParamVo vo);

   /**
    * 查询数据库的appId
    * @return
    */
   String queryAppId ();

   /**
    * 查询微信支付的相关信息
    * @return
    */
   WxRefundBo queryWxAppId ();

   /**
    * 根据订单号查询一条记录
    * @param orderNo
    * @return
    */
   PayInfo queryOne (String orderNo);


}
