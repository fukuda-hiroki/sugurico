package jp.sugurico.sugurico.controller;

import lombok.Data;

// Lombokの@Dataアノテーションで、getter/setterなどを自動生成
@Data
public class UserForm {
    // 画面のフォームのname属性と、ここのフィールド名を一致させる
    private String name;
    private String userName;
    private String loginId;
    private String mail;
    private String password;
}