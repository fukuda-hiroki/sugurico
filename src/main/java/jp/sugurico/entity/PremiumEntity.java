package jp.sugurico.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "premium")
public class PremiumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "premium_id")
    private Long premiumId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(length = 20)
    private String plan;

    @Column(length = 20)
    private String status;

    @Column(name = "first_paid", nullable = false)
    private LocalDateTime firstPaid;

    @Column(name = "limit_date", nullable = false)
    private LocalDateTime limitDate;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}