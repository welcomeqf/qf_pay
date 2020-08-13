package com.dkm.filter;

import com.dkm.entity.ContinKeyVo;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qf
 * @date 2020/3/5
 * @vesion 1.0
 **/
@Component
@Slf4j
public class PostFilter extends ZuulFilter {


   @Override
   public String filterType() {
      return FilterConstants.POST_TYPE;
   }

   @Override
   public int filterOrder() {
      return FilterConstants.SEND_RESPONSE_FILTER_ORDER + 1;
   }

   @Override
   public boolean shouldFilter() {
      return true;
   }

   @Override
   public Object run() throws ZuulException {
      RequestContext context = RequestContext.getCurrentContext();
      Long startDate = (Long) context.get(ContinKeyVo.START_TIME);
      Boolean isSu = (Boolean) context.get(ContinKeyVo.IS_SUCCESS);
      Long time = System.currentTimeMillis() - startDate;
      HttpServletRequest request = context.getRequest();
      log.info(String.format("request: isSuccess=%s time=%sms method=%s uri=%s",isSu,time,request.getMethod(),request.getRequestURI()));
      return null;
   }
}
