package com.s3.mini_s3.exception;

public class BucketNotEmptyException extends StorageException {

    public BucketNotEmptyException(String message) {
        super(message);
    }
}
