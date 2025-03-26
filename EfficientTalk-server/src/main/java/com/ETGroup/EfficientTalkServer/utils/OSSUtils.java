package com.ETGroup.EfficientTalkServer.utils;

import com.ETGroup.EfficientTalkServer.config.oss.OSSConfig;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class OSSUtils {
    @Resource
    private OSSConfig ossConfig;
    
    @Resource
    private MinioClient minIOClient;
    
    /**
     * 获取聊天图片存储bucket名称
     * @return bucket名称
     */
    public String getChatImageBucketName() {
        return ossConfig.getChatImageBucketName();
    }
    
    /**
     * 获取聊天文件存储bucket名称
     * @return bucket名称
     */
    public String getChatFileBucketName() {
        return ossConfig.getChatFileBucketName();
    }
    
    /**
     * 获取群聊图片存储bucket名称
     *
     * @param groupId 群聊id
     *
     * @return bucket名称
     */
    public String getChatGroupImageBucketName(String groupId) {
        return ossConfig.getChatGroupImageBucketName() + "-" + groupId;
    }
    
    /**
     * 获取群聊文件存储bucket名称
     *
     * @param groupId 群聊id
     *
     * @return bucket名称
     */
    public String getChatGroupFileBucketName(String groupId) {
        return ossConfig.getChatGroupFileBucketName() + "-" + groupId;
    }
    
    /**
     * 查看存储bucket是否存在
     *
     * @param bucketName bucket名称
     *
     * @return boolean 是否存在
     */
    public Boolean isBucketExist(String bucketName) {
        boolean found;
        try {
            found = minIOClient.bucketExists(BucketExistsArgs.builder()
                                                             .bucket(bucketName)
                                                             .build());
        }
        catch (Exception e) {
            log.error("操作失败", e);
            return false;
        }
        return found;
    }
    
    /**
     * 创建存储bucket
     *
     * @param bucketName bucket名称
     *
     * @return Boolean
     */
    public Boolean createBucket(String bucketName) {
        try {
            minIOClient.makeBucket(MakeBucketArgs.builder()
                                                 .bucket(bucketName)
                                                 .build());
        }
        catch (Exception e) {
            log.error("操作失败", e);
            return false;
        }
        return true;
    }
    
    /**
     * 删除存储bucket
     *
     * @param bucketName bucket名称
     *
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minIOClient.removeBucket(RemoveBucketArgs.builder()
                                                     .bucket(bucketName)
                                                     .build());
        }
        catch (Exception e) {
            log.error("操作失败", e);
            return false;
        }
        return true;
    }
    
    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minIOClient.listBuckets();
        }
        catch (Exception e) {
            log.error("操作失败", e);
        }
        return null;
    }
    
    
    /**
     * 文件上传
     *
     * @param bucketName bucket名称
     * @param fileId     文件id
     * @param file       文件
     *
     * @return Boolean
     */
    public String upload(String bucketName, String fileId, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new RuntimeException();
        }
        String objectName = fileId + originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                                                    .bucket(bucketName)
                                                    .object(objectName)
                                                    .stream(file.getInputStream(), file.getSize(), -1)
                                                    .contentType(file.getContentType())
                                                    .build();
            //文件名称相同会覆盖
            minIOClient.putObject(objectArgs);
        }
        catch (Exception e) {
            log.error("操作失败", e);
            return null;
        }
        return objectName;
    }
    
    /**
     * 获取文件url
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     *
     * @return String 文件url
     */
    public String getObjectUrl(String bucketName, String fileName) {
        // 查看文件地址
        new GetPresignedObjectUrlArgs();
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                                                                   .bucket(bucketName)
                                                                   .object(fileName)
                                                                   .method(Method.GET)
                                                                   .build();
        try {
            return minIOClient.getPresignedObjectUrl(build);
        }
        catch (Exception e) {
            log.error("操作失败", e);
        }
        return null;
    }
    
    /**
     * 文件下载
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     *
     * @return 文件字节数组
     */
    public byte[] getObjectBlob(String bucketName, String fileName) {
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                                                .bucket(bucketName)
                                                .object(fileName)
                                                .build();
        try (GetObjectResponse response = minIOClient.getObject(objectArgs)) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buf)) != -1) {
                    os.write(buf, 0, len);
                }
                byte[] bytes = os.toByteArray();
                os.flush();
                return bytes;
            }
        }
        catch (Exception e) {
            log.error("操作失败", e);
            return null;
        }
    }
    
    /**
     * 列出bucket内文件对象
     *
     * @param bucketName bucket名称
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects(String bucketName) {
        Iterable<Result<Item>> results = minIOClient.listObjects(
                ListObjectsArgs.builder()
                               .bucket(bucketName)
                               .build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        }
        catch (Exception e) {
            log.error("操作失败", e);
            return null;
        }
        return items;
    }
    
    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     *
     * @return boolean 是否删除成功
     */
    public boolean remove(String bucketName, String fileName) {
        try {
            minIOClient.removeObject(RemoveObjectArgs.builder()
                                                     .bucket(bucketName)
                                                     .object(fileName)
                                                     .build());
        }
        catch (Exception e) {
            log.error("操作失败", e);
            return false;
        }
        return true;
    }
}
