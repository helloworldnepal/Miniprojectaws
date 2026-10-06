package com.s3.mini_s3.repository;

import com.s3.mini_s3.model.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {

Optional<Bucket> findByName(String name);
boolean existsByName(String name);
}
