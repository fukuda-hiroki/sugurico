package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.TagDicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagDicRepository extends JpaRepository<TagDicEntity, Long> {
}