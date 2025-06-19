package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.TagEntity;
import jp.sugurico.sugurico.entity.TagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, TagId> {
}