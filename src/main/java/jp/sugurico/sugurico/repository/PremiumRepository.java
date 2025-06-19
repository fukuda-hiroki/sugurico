package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.PremiumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremiumRepository extends JpaRepository<PremiumEntity, Long> {
}