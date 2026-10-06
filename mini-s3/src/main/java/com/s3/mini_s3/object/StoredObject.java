package com.s3.mini_s3.object;

import com.s3.mini_s3.model.Bucket;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(
        name = "stored_objects",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_bucket_key",
                columnNames = {"bucket_id", "key"}
        )
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoredObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1024)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id", nullable = false)
    private Bucket bucket;

    @Column(nullable = false)
    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "storage_path", nullable = false, length = 512)
    private String storagePath;

    @LastModifiedDate
    @Column(name = "last_modified", nullable = false)
    private Instant lastModified;
}