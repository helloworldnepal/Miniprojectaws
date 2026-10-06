package com.s3.mini_s3.service;


import com.s3.mini_s3.dto.ObjectResponse;
import com.s3.mini_s3.exception.ObjectNotFoundException;
import com.s3.mini_s3.model.Bucket;
import com.s3.mini_s3.object.StoredObject;
import com.s3.mini_s3.repository.BucketRepository;
import com.s3.mini_s3.repository.StoredObjectRepository;

import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObjectService {


    private final StoredObjectRepository objectRepository;
    private final BucketService bucketService;
    private final FileStorageService fileStorageService;
    private final BucketRepository bucketRepository;

    public StoredObject uploadObject(String bucketName, String key, MultipartFile file) {
        Bucket bucket = bucketService.getBucketOrThrow(bucketName);

        String uniqueKey = generateUniqueKey(key, file.getOriginalFilename());

        fileStorageService.store(bucketName, uniqueKey, file);

        StoredObject storedObject = StoredObject.builder()
                .bucket(bucket)
                .key(uniqueKey)
                .size(file.getSize())
                .contentType(file.getContentType())
                .storagePath(bucketName + "/" + uniqueKey)
                .build();

        return objectRepository.save(storedObject);
    }

    private String generateUniqueKey(String key, @Nullable String originalFilename) {

        String extension="";
        if(originalFilename!=null && originalFilename.contains(".")){
          extension=originalFilename.substring(originalFilename.lastIndexOf("."));

        }
        String cleanPrefix= key.endsWith("/")?key:key+"/";

        return  cleanPrefix+UUID.randomUUID().toString()+extension;


    }


    public InputStream downloadObject(String bucketName, String key) {
        Bucket bucket = bucketService.getBucketOrThrow(bucketName);

        objectRepository.findByBucketAndKey(bucket, key)
                .orElseThrow(() -> new ObjectNotFoundException(bucketName, key));

        return fileStorageService.retrieve(bucketName, key);
    }

    public void deleteObject(String bucketName, String key) {
        Bucket bucket = bucketRepository.findByName(bucketName).orElseThrow();

        Optional<StoredObject> bucketAndKey = objectRepository.findByBucketAndKey(bucket, key);

        fileStorageService.delete(bucketName, key);
        objectRepository.delete(bucketAndKey.orElseThrow(() -> new ObjectNotFoundException(bucketName, key)));
    }

    public StoredObject getObjectMetadata(String bucketName, String key) {
        Bucket bucket = bucketService.getBucketOrThrow(bucketName);
        return objectRepository.findByBucketAndKey(bucket, key)
                .orElseThrow(() -> new ObjectNotFoundException(bucketName, key));
    }
    public List<ObjectResponse> listObjects(String bucketName, String prefix) {
        Bucket bucket = bucketService.getBucketOrThrow(bucketName);

        List<StoredObject> objects = (prefix != null && !prefix.isEmpty())
                ? objectRepository.findByBucketIdAndKeyStartingWith(bucket.getId(), prefix)
                : objectRepository.findByBucketId(bucket.getId());

        return objects.stream()
                .map(ObjectResponse::from)
                .toList();
    }

}
