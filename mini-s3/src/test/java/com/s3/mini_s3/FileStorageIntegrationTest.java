package com.s3.mini_s3;


import com.s3.mini_s3.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest

public class FileStorageIntegrationTest {

        @Container
        static GenericContainer<?> minio = new GenericContainer<>(DockerImageName.parse("minio/minio:latest"))
                .withExposedPorts(9000, 9001)
                .withCommand("server", "/data", "--console-address", ":9001")
                .withEnv("MINIO_ROOT_USER", "minioadmin")
                .withEnv("MINIO_ROOT_PASSWORD", "minioadmin");

        @DynamicPropertySource
        static void overrideProps(DynamicPropertyRegistry registry) {
            registry.add("minio.url", () -> "http://" + minio.getHost() + ":" + minio.getMappedPort(9000));
            registry.add("minio.access-key", () -> "minioadmin");
            registry.add("minio.secret-key", () -> "minioadmin");
        }

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    void storesAndRetrievesFile() throws Exception {
        String bucketName = "integration-bucket";
        fileStorageService.createBucket(bucketName);

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "hello world".getBytes());

        fileStorageService.store(bucketName, "test.txt", file);

        InputStream retrieved = fileStorageService.retrieve(bucketName, "test.txt");
        String content = new String(retrieved.readAllBytes());

        assertThat(content).isEqualTo("hello world");
    }
    }
