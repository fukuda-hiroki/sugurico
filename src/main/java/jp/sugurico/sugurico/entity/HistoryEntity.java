package jp.sugurico.sugurico.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "history")
@IdClass(HistoryId.class) // 複合主キークラスを指定
public class HistoryEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id")
    private ForumEntity forum;

    @Column(name = "first_view_at", nullable = false)
    private LocalDateTime firstViewAt;

    @Column(name = "last_view_at", nullable = false)
    private LocalDateTime lastViewAt;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;
}