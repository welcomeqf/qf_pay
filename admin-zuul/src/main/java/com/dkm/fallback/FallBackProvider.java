package com.dkm.fallback;

import com.alibaba.fastjson.JSON;
import com.dkm.entity.ContinKeyVo;
import com.dkm.entity.ResultVo;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author qf
 * @date 2020/3/5
 * @vesion 1.0
 **/
@Component
@Slf4j
public class FallBackProvider implements FallbackProvider {


   @Override
   public String getRoute() {
      return "*";
   }

   @Override
   public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
      RequestContext context = RequestContext.getCurrentContext();
      context.set(ContinKeyVo.IS_SUCCESS,false);
      cause.printStackTrace();
      return new ClientHttpResponse() {
         @Override
         public HttpStatus getStatusCode() throws IOException {
            //外部请求请求网关已经成功，但是网关转发其他服务的请求失败，
            //不应该把api的404,500等问题抛给客户端
            //网关和api服务集群对于客户端来说是黑盒子
            return HttpStatus.OK;
         }

         @Override
         public int getRawStatusCode() throws IOException {
            return HttpStatus.OK.value();
         }

         @Override
         public String getStatusText() throws IOException {
            return HttpStatus.OK.getReasonPhrase();
         }

         @Override
         public void close() {

         }

         @Override
         public InputStream getBody() throws IOException {
            ResultVo vo = ResultVo.fall("服务路由异常");
            String jsonResult = JSON.toJSONString(vo);
            return new ByteArrayInputStream(jsonResult.getBytes("UTF-8"));
         }

         @Override
         public HttpHeaders getHeaders() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            return httpHeaders;
         }
      };
   }
}
