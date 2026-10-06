package com.s3.mini_s3.service;


import com.s3.mini_s3.model.Bucket;
import com.s3.mini_s3.repository.BucketRepository;
import com.s3.mini_s3.exception.BucketAlreadyExistsException;
import com.s3.mini_s3.exception.BucketNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final FileStorageService fileStorageService;
    private final BucketRepository bucketRepository;


    public Bucket createBucket(String bucketName) {

        if (bucketRepository.existsByName(bucketName)) {
            throw new BucketAlreadyExistsException(bucketName);
        }

        fileStorageService.createBucket(bucketName);
        Bucket bucket = new Bucket();
        bucket.setName(bucketName);
        return bucketRepository.save(bucket);


    }
    public Bucket getBucketOrThrow(String name) {
        return bucketRepository.findByName(name)
                .orElseThrow(() -> new BucketNotFoundException(name));
    }

    public List<Bucket> listBuckets() {
        return bucketRepository.findAll();
    }


    public void deleteBucket(String bucketName) {

        bucketRepository.findByName(bucketName).ifPresent(bucket -> {
            fileStorageService.deleteBucket(bucketName);
            bucketRepository.delete(bucket);
        });

    }





}