package jp.sugurico.sugurico.controller;

import lombok.Data;

@Data
public class ForumForm {
    private String title;
    private String text;
    //  今後の拡張用:タグ、公開期限など
}
