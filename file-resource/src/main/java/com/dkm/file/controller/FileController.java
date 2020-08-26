package com.dkm.file.controller;

import com.dkm.constanct.CodeType;
import com.dkm.entity.vo.FileVo;
import com.dkm.exception.ApplicationException;
import com.dkm.file.service.IFileService;
import com.dkm.jwt.islogin.CheckToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author qf
 * @date 2020/4/11
 * @vesion 1.0
 **/
@Api(tags = "上传文件api")
@Slf4j
@RestController
@RequestMapping("/v1/file")
public class FileController {

  @Autowired
  private IFileService fileService;

   /**
    * 上传文件图片到本地工程
    * @param file
    * @return
    * @throws Exception
    */
   @ApiOperation(value = "storeFile",notes = "图片上传")
   @ApiImplicitParam(name = "Token",value = "Token",required = true,dataType = "String",paramType = "header")
   @PostMapping("/storeFile")
   @CrossOrigin
   @CheckToken
   public FileVo storeFile(@RequestBody MultipartFile file) {
      if (file == null) {
         throw new ApplicationException(CodeType.PARAMETER_ERROR, "未上传文件");
      }
      return fileService.storeFile(file);
   }

   /**
    * Feign调用  不提供外部
    * @param content
    * @return
    */
   @GetMapping("/getQrCode")
   public FileVo getQrCode(@RequestParam("content") String content) {
      return fileService.getQrCode (content);
   }

}
