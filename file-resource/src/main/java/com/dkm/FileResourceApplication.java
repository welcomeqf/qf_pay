package com.dkm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qf
 */
@EnableSwagger2
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class FileResourceApplication extends SpringBootServletInitializer {

   public static void main(String[] args) {
      SpringApplication.run(FileResourceApplication.class, args);
   }

   /**
    * 打包
    * @param builder
    * @return
    */
   @Override
   protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
      return builder.sources(FileResourceApplication.class);
   }

}
