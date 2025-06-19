package jp.sugurico.sugurico.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tag_dic")
public class TagDicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag_name", length = 50)
    private String tagName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}