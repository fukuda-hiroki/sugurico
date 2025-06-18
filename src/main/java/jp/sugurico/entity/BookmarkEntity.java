package jp.sugurico.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookmark")
@IdClass(BookmarkId.class) // 複合主キークラスを指定
public class BookmarkEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private ForumEntity forum;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}