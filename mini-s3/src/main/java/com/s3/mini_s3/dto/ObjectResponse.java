package com.s3.mini_s3.dto;

import com.s3.mini_s3.object.StoredObject;
import org.springframework.web.servlet.DispatcherServlet;

import java.time.Instant;


public record ObjectResponse(
        String bucket,
        String key,
        long size,
        String contentType,
        Instant lastModified) {


    public static ObjectResponse from(StoredObject storedObject) {

        return new ObjectResponse(
                storedObject.getBucket().getName(),
                storedObject.getKey(),
                storedObject.getSize(),
                storedObject.getContentType(),
                storedObject.getLastModified()

        );





}}