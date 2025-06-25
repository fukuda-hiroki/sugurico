package jp.sugurico.sugurico.service;

import jp.sugurico.sugurico.entity.ForumEntity;
import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.repository.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
public class ForumService {
    @Autowired
    private ForumRepository forumRepository;

    @Transactional
    public void createPost(ForumEntity forumEntity, UserEntity user){
        forumEntity.setUser(user); // 投稿にユーザー情報をセット
        // ▼▼▼ ここからが追記・修正部分 ▼▼▼

        forumEntity.setUser(user); // 投稿にユーザー情報をセット

        // ★ viewCount と deleteFlag に初期値を設定する
        forumEntity.setViewCount(0);
        forumEntity.setDeleteFlag(false);

        // ▲▲▲ ここまでが追記・修正部分 ▲▲▲
        forumRepository.save(forumEntity);
    }
}
