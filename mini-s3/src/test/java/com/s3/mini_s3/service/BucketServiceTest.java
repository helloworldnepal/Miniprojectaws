package com.s3.mini_s3.service;


import com.s3.mini_s3.exception.BucketAlreadyExistsException;
import com.s3.mini_s3.exception.BucketNotFoundException;
import com.s3.mini_s3.model.Bucket;
import com.s3.mini_s3.repository.BucketRepository;
import com.s3.mini_s3.repository.StoredObjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BucketServiceTest {

    @Mock
    private BucketRepository bucketRepository;

    @Mock
    private StoredObjectRepository objectRepository;

    @Mock
    private FileStorageService fileStorageService;


     @InjectMocks
     private BucketService bucketService;


     @Test
     void createBucket_saveBucket_whenNameIsUnique() {

         when(bucketRepository.existsByName("my-photos")).thenReturn(false);
         when(bucketRepository.save(any(Bucket.class)))
                 .thenAnswer(invocation -> invocation.getArgument(0));

         Bucket result = bucketService.createBucket("my-photos");

         assertThat(result.getName()).isEqualTo("my-photos");
         verify(fileStorageService).createBucket("my-photos");
         verify(bucketRepository).save(any(Bucket.class));

     }

@Test
     void createbucket_throws_name_alreadyExist(){


         when(bucketRepository.existsByName("my-photos")).thenReturn(true);
         assertThatThrownBy(()-> bucketService.createBucket("my-photos"))
                 .isInstanceOf(BucketAlreadyExistsException.class);
         verify(fileStorageService,never()).createBucket(anyString());
         verify(bucketRepository,never()).save(any(Bucket.class));

     }



    @Test
    void getBucketOrThrow_throws_whenBucketMissing() {
        when(bucketRepository.findByName("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bucketService.getBucketOrThrow("ghost"))
                .isInstanceOf(BucketNotFoundException.class);
    }




}
