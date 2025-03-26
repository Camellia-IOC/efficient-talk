package com.ETGroup.EfficientTalkServer.config.oss;

import io.minio.MinioClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {
    
    @Value("${minio.endpoint}")
    private String minioEndpoint;
    
    @Value("${minio.access-key}")
    private String minioAccessKey;
    
    @Value("${minio.secret-key}")
    private String minioSecretKey;
    
    @Getter
    @Value("${minio.org-cloud-disk-bucket}")
    private String orgCloudDiskBucketName;
    
    @Getter
    @Value("${minio.chat-file-bucket}")
    private String chatFileBucketName;
    
    @Getter
    @Value("${minio.chat-image-bucket}")
    private String chatImageBucketName;
    
    @Getter
    @Value("${minio.chat-group-file-bucket}")
    private String chatGroupFileBucketName;
    
    @Getter
    @Value("${minio.chat-group-image-bucket}")
    private String chatGroupImageBucketName;
    
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                          .endpoint(minioEndpoint)
                          .credentials(minioAccessKey, minioSecretKey)
                          .build();
    }
}