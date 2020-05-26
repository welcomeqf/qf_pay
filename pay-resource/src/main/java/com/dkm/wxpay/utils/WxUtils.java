package com.dkm.wxpay.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayXmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.security.provider.MD5;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author qf
 * @date 2020/3/19
 * @vesion 1.0
 **/
@Slf4j
public class WxUtils {

//   public static final String APPID = "wxa5c9572cb207cd5d";
//
//   public static final String MCHID = "1546806081";
//
//   public static final String PATERNERKEY = "pL7ZKqYxqH65KAmnpSZPKtp3kcy0Pug6";

//   public static final String APPID = "wxeb07fe802c6da946";
////
//   public static final String MCHID = "1586455861";
//
//   public static final String PATERNERKEY = "JBqeTQCigJJSl3BtPUbdSl0prStjM54Q";


   /**
    * 流转String
    * @param tInputStream
    * @return
    */
   public static String getStreamToStr(InputStream tInputStream) {
      if (tInputStream != null) {
         try {
            BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
            StringBuffer tStringBuffer = new StringBuffer();
            String sTempOneLine;
            while ((sTempOneLine = tBufferedReader.readLine()) != null) {
               tStringBuffer.append(sTempOneLine);
            }
            return tStringBuffer.toString();
         } catch (Exception ex) {
            ex.printStackTrace();
         }
      }
      return null;
   }

   /**
    * 元转分
    * @param price
    * @return
    */
   public static Integer changeY2F(double price) {
      DecimalFormat df = new DecimalFormat("#.00");
      price = Double.parseDouble(df.format(price));
      Integer money = (int) (price * 100);
      return money;
   }


   public static String doPostSSL(String url,String xml,String mch_id,String certUrl) throws Exception {
      // 证书
      char[] password = mch_id.toCharArray();
      InputStream certStream = new FileInputStream(certUrl);//读取证书
      KeyStore ks = KeyStore.getInstance("PKCS12");
      ks.load(certStream, password);

      // 实例化密钥库 & 初始化密钥工厂
      KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      kmf.init(ks, password);

      // 创建 SSLContext
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

      SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
            sslContext,
            new String[]{"TLSv1"},
            null,
            new DefaultHostnameVerifier());

      BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
            RegistryBuilder.<ConnectionSocketFactory>create()
                  .register("http", PlainConnectionSocketFactory.getSocketFactory())
                  .register("https", sslConnectionSocketFactory)
                  .build(),
            null,
            null,
            null
      );
      CloseableHttpClient httpClient = null;
      CloseableHttpResponse httpResponse = null;
      // 创建httpClient实例
      httpClient = HttpClients.custom().setConnectionManager(connManager).build();
      // 创建httpPost远程连接实例
      HttpPost httpPost = new HttpPost(url);
      // 配置请求参数实例
      RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
            .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
            .setSocketTimeout(60000)// 设置读取数据连接超时时间
            .build();
      // 为httpPost实例设置配置
      httpPost.setConfig(requestConfig);
      // 设置请求头
      httpPost.addHeader("Content-Type", "text/xml");
      httpPost.addHeader("User-Agent", "WXPAYSDK_VERSION" + " " + mch_id);
      // 封装post的JSON请求参数
      httpPost.setEntity(new StringEntity(xml, "UTF-8"));
      try {
         // httpClient对象执行post请求,并返回响应参数对象
         httpResponse = httpClient.execute(httpPost);
         // 从响应对象中获取响应内容
         HttpEntity entity = httpResponse.getEntity();
         String  result = EntityUtils.toString(entity,"utf-8");
         return result;
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         // 关闭资源
         if (null != httpResponse) {
            try {
               httpResponse.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         if (null != httpClient) {
            try {
               httpClient.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
      return null;
   }





}
