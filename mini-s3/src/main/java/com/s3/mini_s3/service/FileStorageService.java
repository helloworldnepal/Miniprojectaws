package com.s3.mini_s3.service;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface FileStorageService {

    void store (String bucketName, String objectKey,MultipartFile file);


    InputStream retrieve(String bucketName, String objectKey);


    void delete(String bucketName, String objectKey);


    boolean bucketExists(String bucketName);



    void createBucket(String bucketName);




    void deleteBucket(String bucketName);

}
