package com.s3.mini_s3.controller;

import com.s3.mini_s3.dto.ObjectResponse;
import com.s3.mini_s3.object.StoredObject;
import com.s3.mini_s3.service.ObjectService;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/buckets")
@RequiredArgsConstructor
public class ObjectController {

    private final ObjectService objectService;

    @PutMapping("/{bucketName}/objects/{*key}")
    public ResponseEntity<ObjectResponse> upload(
            @PathVariable String bucketName,
            @PathVariable String key,
            @RequestParam("file") MultipartFile file) {

        StoredObject saved = objectService.uploadObject(bucketName, normalize(key), file);
        return ResponseEntity.status(HttpStatus.CREATED).body(ObjectResponse.from(saved));
    }

    @GetMapping("/{bucketName}/objects/{*key}")
    public ResponseEntity<InputStreamResource> download(
            @PathVariable String bucketName,
            @PathVariable String key) throws ServerException,
            InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        String objectKey = normalize(key);
        StoredObject metadata = objectService.getObjectMetadata(bucketName, objectKey);
        InputStream inputStream = objectService.downloadObject(bucketName, objectKey);

        String contentType = metadata.getContentType() != null
                ? metadata.getContentType()
                : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(metadata.getSize())
                .body(new InputStreamResource(inputStream));
    }


    @RequestMapping(value = "/{bucketName}/objects/{*key}", method = RequestMethod.HEAD)
    public ResponseEntity<?> head( @PathVariable String bucketName,
                                   @PathVariable String key){

        StoredObject objectMetadata = objectService.getObjectMetadata(bucketName, normalize(key));
        String s = objectMetadata.getContentType() != null ? objectMetadata.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(s))
                .contentLength(objectMetadata.getSize())
                .build();


    }

    @GetMapping("/{bucketName}/objects")
    public ResponseEntity<List<ObjectResponse>> list(
            @PathVariable String bucketName,
            @RequestParam(required = false) String prefix) {

        return ResponseEntity.ok(objectService.listObjects(bucketName, prefix));
    }


    private String normalize(String key) {
        return key.startsWith("/") ? key.substring(1) : key;
    }
}