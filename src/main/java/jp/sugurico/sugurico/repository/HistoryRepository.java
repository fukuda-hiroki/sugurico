package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.HistoryEntity;
import jp.sugurico.sugurico.entity.HistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, HistoryId> {
}