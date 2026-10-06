package com.s3.mini_s3.repository;

import com.s3.mini_s3.model.Bucket;
import com.s3.mini_s3.object.StoredObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoredObjectRepository extends JpaRepository<StoredObject, Long> {


    Optional<StoredObject> findByBucketAndKey(Bucket bucket, String key);
    List<StoredObject> findByBucketId(Long bucketId);

    @Query("SELECT so FROM StoredObject so WHERE so.bucket.id = :bucketId AND so.key LIKE :prefix || '%'")
    List<StoredObject> findByBucketIdAndKeyStartingWith(@Param("bucketId") Long bucketId, @Param("prefix") String prefix);

    void deleteByBucketIdAndKey(Long bucketId, String key);
    boolean existsByBucketId(Long bucketId);
}
