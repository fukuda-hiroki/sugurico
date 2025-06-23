package jp.sugurico.sugurico.repository;

import jp.sugurico.sugurico.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // ★インポートを追加

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // ★ログインIDを元にユーザーを検索するメソッドを追加
    // findBy + フィールド名 という命名規則に従うと、Spring Data JPAが自動でSQLを生成してくれる
    Optional<UserEntity> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
    boolean existsByMail(String mail);
}