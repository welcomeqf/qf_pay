package com.dkm.filter;

import com.dkm.entity.ContinKeyVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2020/3/5
 * @vesion 1.0
 **/
@Component
@Slf4j
public class PreFilter extends ZuulFilter {

   @Override
   public String filterType() {
      return FilterConstants.PRE_TYPE;
   }

   @Override
   public int filterOrder() {
      return FilterConstants.PRE_DECORATION_FILTER_ORDER;
   }

   @Override
   public boolean shouldFilter() {
      return true;
   }

   @Override
   public Object run() throws ZuulException {

      //获取上下文
      RequestContext context = RequestContext.getCurrentContext();
      //进入时候的时间
      context.set(ContinKeyVo.START_TIME,System.currentTimeMillis());
      //代表成功
      context.set(ContinKeyVo.IS_SUCCESS,true);
      log.info("go_in gateway..");

      return null;
   }
}
