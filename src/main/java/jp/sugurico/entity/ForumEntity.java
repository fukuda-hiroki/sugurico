package jp.sugurico.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "forums")
public class ForumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forum_id")
    private Long forumId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(length = 140)
    private String text;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

    @Column(name = "delete_flag", nullable = false)
    private boolean deleteFlag;
}