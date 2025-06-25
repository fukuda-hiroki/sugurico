package jp.sugurico.sugurico.service;

import jp.sugurico.sugurico.entity.ForumEntity;
import jp.sugurico.sugurico.entity.TagDicEntity;
import jp.sugurico.sugurico.entity.TagEntity;
import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.repository.ForumRepository;
import jp.sugurico.sugurico.repository.TagDicRepository;
import jp.sugurico.sugurico.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ForumService {
    @Autowired
    private ForumRepository forumRepository;
    @Autowired
    private TagDicRepository tagDicRepository;
    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public void createPost(ForumEntity forumEntity, String expires, List<String> tags , UserEntity user){
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


        ForumEntity savedForum = forumRepository.save(forumEntity);

        //  タグの処理
        if(tags != null) {
            for(String tagName : tags) {
                if(tagName == null || tagName.trim().isEmpty()){
                    continue;
                }
                String trimmedTagName = tagName.trim();

                //  1:tag_dicテーブルに同じ名前のタグがあるか検索
                Optional<TagDicEntity> existingTag = tagDicRepository.findByTagName(trimmedTagName);

                TagDicEntity tagToLink;
                if(existingTag.isPresent()) {
                    //  2a:もし存在すれば、そのタグを使う
                    tagToLink = existingTag.get();
                } else {
                    //  2b:もし存在しなければ新しく作成して保存
                    TagDicEntity newTag = new TagDicEntity();
                    newTag.setTagName(trimmedTagName);
                    newTag.setCreatedAt(LocalDateTime.now());
                    tagToLink = tagDicRepository.save(newTag);
                }

                //  3:tagテーブル(中間テーブル)で、投稿とタグを紐づける
                TagEntity tagLink = new TagEntity();
                tagLink.setForum(savedForum);   //  保存済みの投稿Entityをセット
                tagLink.setTag(tagToLink);  //  紐づけるタグEntityをセット
                tagRepository.save(tagLink);
            }
        }
    }
}
