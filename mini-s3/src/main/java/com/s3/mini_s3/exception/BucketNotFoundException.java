package com.s3.mini_s3.exception;

public class BucketNotFoundException extends StorageException {

    public BucketNotFoundException(String message) {
        super(message);
    }
}
