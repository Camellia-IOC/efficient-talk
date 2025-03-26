package com.ETGroup.EfficientTalkServer.utils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class FileUtils {
    @Resource
    private OSSUtils ossUtils;
    
    /**
     * 获取文件Blob
     *
     * @param filePath 文件路径
     *
     * @return 文件Blob
     */
    public ResponseEntity<byte[]> getFileBlob(String filePath) {
        if (filePath == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            File file = new File(filePath);
            byte[] fileContent = new byte[(int) file.length()];
            
            // 读取文件内容
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(fileContent);
                fileInputStream.close();
            }
            catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                                                            .filename(URLEncoder.encode(file.getName(), StandardCharsets.UTF_8))
                                                            .build());
            headers.setContentType(MediaType.parseMediaType("application/octet-stream;charset=UTF-8"));
            
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        }
    }
    
    /**
     * 获取文件Blob
     *
     * @param bucketName bucket名称
     * @param fileId 文件ID
     *
     * @return 文件Blob
     */
    public ResponseEntity<byte[]> getOSSFileBlob(String bucketName, String fileId, String fileName) {
        if (!ossUtils.isBucketExist(bucketName)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            byte[] fileContent;
            // 读取文件内容
            try {
                fileContent = ossUtils.getObjectBlob(bucketName, fileId);
            }
            catch (Exception e) {
                log.error("操作失败", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                                                            .filename(URLEncoder.encode(fileName, StandardCharsets.UTF_8))
                                                            .build());
            headers.setContentType(MediaType.parseMediaType("application/octet-stream;charset=UTF-8"));
            
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        }
    }
}
