package com.s3.mini_s3.service;


import com.s3.mini_s3.exception.ObjectNotFoundException;
import com.s3.mini_s3.model.Bucket;
import com.s3.mini_s3.object.StoredObject;
import com.s3.mini_s3.repository.BucketRepository;
import com.s3.mini_s3.repository.StoredObjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObjectServiceTest {




    @Mock
    private StoredObjectRepository objectRepository;

    @Mock
    private BucketService bucketService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private BucketRepository bucketRepository;

    @InjectMocks
    private ObjectService objectService;
    


@Test
    void uploadObject_generateUniqueKey_savesMetadata(){
        
        Bucket bucket = Bucket.builder().id(1L).name("my-photos").build();
        when(bucketService.getBucketOrThrow("my-photos")).thenReturn(bucket);
        when(objectRepository.save(any(StoredObject.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MockMultipartFile file=new MockMultipartFile("file","photo.png","image/png","fake-image-bytes".getBytes());
    StoredObject result = objectService.uploadObject("my-photos","images",file);
    
    assertThat(result.getKey()).startsWith("images/");
    assertThat(result.getKey()).endsWith("png");
    assertThat(result.getContentType()).isEqualTo("image/png");
    assertThat(result.getSize()).isEqualTo(file.getSize());
    verify(fileStorageService).store(eq("my-photos"), anyString(), eq(file));
    
    

    }
    @Test
    void downloadObject_throws_whenObjectMissing() {
        Bucket bucket = Bucket.builder().id(1L).name("my-photos").build();
        when(bucketService.getBucketOrThrow("my-photos")).thenReturn(bucket);
        when(objectRepository.findByBucketAndKey(bucket, "images/missing.png"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                objectService.downloadObject("my-photos", "images/missing.png"))
                .isInstanceOf(ObjectNotFoundException.class);

        verify(fileStorageService, never()).retrieve(anyString(), anyString());
    }


    @Test
    void downloadObject_returnsStream_whenObjectExists() {
        Bucket bucket = Bucket.builder().id(1L).name("my-photos").build();
        StoredObject stored = StoredObject.builder().key("images/a.png").bucket(bucket).size(10l).build();
        when(bucketService.getBucketOrThrow("my-photos")).thenReturn(bucket);
        when(objectRepository.findByBucketAndKey(bucket, "images/a.png"))
                .thenReturn(Optional.of(stored));
        when(fileStorageService.retrieve("my-photos","images/a.png")).thenReturn(new ByteArrayInputStream("data".getBytes()));
        var stream = objectService.downloadObject("my-photos", "images/a.png");

        assertThat(stream).isNotNull();

    }
}

