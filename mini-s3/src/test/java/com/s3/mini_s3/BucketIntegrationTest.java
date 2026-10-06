package com.s3.mini_s3;

import com.s3.mini_s3.model.Bucket;
import com.s3.mini_s3.repository.BucketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest
public class BucketIntegrationTest {


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private BucketRepository bucketRepository;

    @Test
    void savesBucket_andReadsItBack() {
        Bucket bucket = Bucket.builder().name("integration-test-bucket").build();
        bucketRepository.save(bucket);

        var found = bucketRepository.findByName("integration-test-bucket");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("integration-test-bucket");
    }
}
