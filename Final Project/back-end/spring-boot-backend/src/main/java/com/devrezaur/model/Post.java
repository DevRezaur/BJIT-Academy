package com.devrezaur.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "posts")
@Component
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long postId;

    @Column(name = "batch_id")
    private int batchId;

    @Column(name = "userId")
    private String userId;

    @Column(name = "description", columnDefinition = "text", length = 10485760)
    private String description;

    @Column(name = "resource_name")
    private String resourceName;

    @Column(name = "resources_link")
    private String resourcesLink;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

}
