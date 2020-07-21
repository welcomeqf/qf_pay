package com.dkm.data;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author qf
 * @date 2020/7/20
 * @vesion 1.0
 **/
@Component
public class PaginationPageUtils {

   @Bean
   public PaginationInterceptor paginationInterceptor() {
      return new PaginationInterceptor();
   }
}
