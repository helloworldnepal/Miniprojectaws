package com.s3.mini_s3.controller;

import com.s3.mini_s3.model.Bucket;
import com.s3.mini_s3.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buckets")
@RequiredArgsConstructor
public class BucketController {

    private final BucketService bucketService;

    @PutMapping("/{bucketName}")
    public ResponseEntity<Bucket> createBucket(@PathVariable String bucketName) {
        Bucket bucket = bucketService.createBucket(bucketName);
        return ResponseEntity.status(HttpStatus.CREATED).body(bucket);
    }

    @GetMapping
    public ResponseEntity<List<Bucket>> listBuckets() {
        return ResponseEntity.ok(bucketService.listBuckets());
    }

    @DeleteMapping("/{bucketName}")
    public ResponseEntity<Void> deleteBucket(@PathVariable String bucketName) {
        bucketService.deleteBucket(bucketName);
        return ResponseEntity.noContent().build();
    }

}
