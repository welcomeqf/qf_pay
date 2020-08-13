package com.dkm.filter;

import com.dkm.constanct.CodeType;
import com.dkm.exception.ApplicationException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2020/3/6
 * @vesion 1.0
 **/
@Component
@Slf4j
public class ErroFilter extends ZuulFilter {

   @Override
   public String filterType() {
      return FilterConstants.ERROR_TYPE;
   }

   @Override
   public int filterOrder() {
      return FilterConstants.DEBUG_FILTER_ORDER;
   }

   @Override
   public boolean shouldFilter() {
      return true;
   }

   @Override
   public Object run() throws ZuulException {

      log.info("gateway err.");

      throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
   }
}
