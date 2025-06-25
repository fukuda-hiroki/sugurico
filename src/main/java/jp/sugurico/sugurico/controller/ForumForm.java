package jp.sugurico.sugurico.controller;

import lombok.Data;

import java.util.List;

@Data
public class ForumForm {
    private String title;
    private String text;
    private String expires;
    private List<String> tags;
    //  今後の拡張用:タグ、公開期限など
}
