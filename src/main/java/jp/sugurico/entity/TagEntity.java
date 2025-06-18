package jp.sugurico.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tag")
@IdClass(TagId.class) // 複合主キークラスを指定
public class TagEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id")
    private ForumEntity forum;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private TagDicEntity tag;
}