package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.TagDicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagDicRepository extends JpaRepository<TagDicEntity, Long> {
    Optional<TagDicEntity> findByTagName(String tagName);

    List<TagDicEntity>findByTagNameStartingWith(String prefix);
}