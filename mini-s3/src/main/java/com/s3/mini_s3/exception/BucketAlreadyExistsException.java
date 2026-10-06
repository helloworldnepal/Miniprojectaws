package com.s3.mini_s3.exception;

public class BucketAlreadyExistsException extends StorageException {

    public BucketAlreadyExistsException(String message) {
        super(message);
    }
}
