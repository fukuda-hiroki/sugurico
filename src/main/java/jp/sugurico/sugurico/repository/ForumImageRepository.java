package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.ForumImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumImageRepository extends JpaRepository<ForumImageEntity, Long> {
}