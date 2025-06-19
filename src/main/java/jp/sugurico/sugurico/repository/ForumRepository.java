package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.ForumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<ForumEntity, Long> {
}