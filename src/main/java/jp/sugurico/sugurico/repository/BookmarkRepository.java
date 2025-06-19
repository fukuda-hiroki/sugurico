package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.BookmarkEntity;
import jp.sugurico.sugurico.entity.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, BookmarkId> {
}