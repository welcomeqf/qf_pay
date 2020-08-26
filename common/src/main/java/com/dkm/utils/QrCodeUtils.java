package com.dkm.utils;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;


/**
 * @author qf
 */
@Component
public class QrCodeUtils {


	public boolean createQrCode(File file, String content) {
      //图片的宽度
		int width=300;
      //图片的高度
      int height=300;
      //图片的格式
      String format="png";

        /**
         * 定义二维码的参数
         */
      HashMap hints=new HashMap();
      //指定字符编码为“utf-8”
      hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
      //指定二维码的纠错等级为中级
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
      //设置图片的边距
      hints.put(EncodeHintType.MARGIN, 2);

        /**
         * 生成二维码
         */
        try {
            BitMatrix bitMatrix=new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height,hints);
            Path filePath=file.toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}

	public String getContentFromQrCode(String filePath) {
		MultiFormatReader formatReader=new MultiFormatReader();
        File file=new File(filePath);
        BufferedImage image;
        try {
            image = ImageIO.read(file);
            BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer
                                    (new BufferedImageLuminanceSource(image)));
            HashMap hints=new HashMap();
           //指定字符编码为“utf-8”
            hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
            Result result=formatReader.decode(binaryBitmap,hints);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}

   /**
    * 生成二维码
    * @param text
    * @param filePath
    * @throws WriterException
    * @throws IOException
    */
   public void generateQrCodeImage(String text, String filePath) throws WriterException, IOException {
      QRCodeWriter qrCodeWriter = new QRCodeWriter();

      BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);

      Path path = FileSystems.getDefault().getPath(filePath);

      MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

   }

}
