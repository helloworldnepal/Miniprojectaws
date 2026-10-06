package com.s3.mini_s3.exception;


public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String bucketName, String key) {
        super("Object not found: " + key + " in bucket: " + bucketName);
    }
}