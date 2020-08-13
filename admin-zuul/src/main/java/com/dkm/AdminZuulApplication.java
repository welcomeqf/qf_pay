package com.dkm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

/**
 * @author qf
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableZuulProxy
public class AdminZuulApplication {

   public static void main(String[] args) {
      SpringApplication.run(AdminZuulApplication.class, args);
   }

}
