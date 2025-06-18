package jp.sugurico.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mutes")
@IdClass(MuteId.class) // 複合主キークラスを指定
public class MuteEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "muting_user_id")
    private UserEntity mutingUser;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "muted_user_id")
    private UserEntity mutedUser;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}