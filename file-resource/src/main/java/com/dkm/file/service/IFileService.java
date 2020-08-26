package com.dkm.file.service;

import com.dkm.entity.vo.FileVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author qf
 * @date 2020/5/15
 * @vesion 1.0
 **/
public interface IFileService {


   /**
    *  上传图片
    * @param file
    * @return
    */
   FileVo storeFile(MultipartFile file);


   /**
    *  上传二维码或图片
    * @param file
    * @return
    */
   FileVo storeQrCode(File file);

   /**
    *  生成二维码并上传服务器
    * @param content
    * @return
    */
   FileVo getQrCode(String content);
}
