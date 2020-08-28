package com.dkm.projectfig;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.dkm.projectfig.filter.CustomerValueFilter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qf
 * @date 2020/8/26
 * @vesion 1.0
 **/
@Configuration
public class JackJsonToFastConfig {

   @Bean
   public HttpMessageConverters customConverters() {
      FastJsonConfig fastJsonConfig = new FastJsonConfig();
      fastJsonConfig.setSerializerFeatures(
            SerializerFeature.PrettyFormat,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty
      );
      // 添加自定义值处理器
      fastJsonConfig.setSerializeFilters(new CustomerValueFilter());
      fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
      FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
      fastConverter.setFastJsonConfig(fastJsonConfig);

      List<MediaType> supportedMediaTypes = new ArrayList<>();
      supportedMediaTypes.add(MediaType.APPLICATION_JSON);
      supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
      supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
      supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
      supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
      supportedMediaTypes.add(MediaType.APPLICATION_PDF);
      supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
      supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
      supportedMediaTypes.add(MediaType.APPLICATION_XML);
      supportedMediaTypes.add(MediaType.IMAGE_GIF);
      supportedMediaTypes.add(MediaType.IMAGE_JPEG);
      supportedMediaTypes.add(MediaType.IMAGE_PNG);
      supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
      supportedMediaTypes.add(MediaType.TEXT_HTML);
      supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
      supportedMediaTypes.add(MediaType.TEXT_PLAIN);
      supportedMediaTypes.add(MediaType.TEXT_XML);

      fastConverter.setSupportedMediaTypes(supportedMediaTypes);
      HttpMessageConverter<?> converter = fastConverter;
      return new HttpMessageConverters(converter);
   }
}
