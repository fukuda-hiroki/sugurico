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
    public void createPost(ForumEntity forumEntity, String expires, UserEntity user){
        forumEntity.setUser(user); // 投稿にユーザー情報をセット
        // ★ viewCount と deleteFlag に初期値を設定する
        forumEntity.setViewCount(0);
        forumEntity.setDeleteFlag(false);

        LocalDateTime now = LocalDateTime.now();
        if(expires != null){
            switch (expires) {
                case "1day":forumEntity.setDeleteDate(now.plusDays(1));
                    break;
                case "2days":forumEntity.setDeleteDate(now.plusDays(2));
                    break;
                case "3days":forumEntity.setDeleteDate(now.plusDays(3));
                    break;
                case "4days":forumEntity.setDeleteDate(now.plusDays(4));
                    break;
                case "5days":forumEntity.setDeleteDate(now.plusDays(5));
                    break;
                case "6days":forumEntity.setDeleteDate(now.plusDays(6));
                    break;
                case "7days":forumEntity.setDeleteDate(now.plusDays(7));
                    break;
                case "permanent":
                default:
                    //  永続のまま
                    break;




            }
        }


        forumRepository.save(forumEntity);
    }
}
