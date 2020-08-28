package com.dkm.projectfig.filter;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.math.BigDecimal;

/**
 * @author qf
 * @date 2020/8/26
 * @vesion 1.0
 **/
public class CustomerValueFilter implements ValueFilter {


   @Override
   public Object process(Object object, String name, Object value) {
      // long 转换为 string
      if (value instanceof Long) {
         return value.toString();
      }

      // bigDecimal 保留两位小数
      if (value instanceof BigDecimal) {
         return String.format("%.2f", ((BigDecimal) value).doubleValue());
      }

      return value;
   }
}
