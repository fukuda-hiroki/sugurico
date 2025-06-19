package jp.sugurico.sugurico.controller;

import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // このクラスがWebリクエストを処理するControllerであることを示す
public class UserController {

    @Autowired // UserServiceのインスタンスを自動で注入
    private UserService userService;

    /**
     * ユーザー登録画面を表示するためのメソッド
     * GETリクエストで /register にアクセスされた時に呼ばれる
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        // "user/register.html" というThymeleafテンプレートを表示する
        return "user/register";
    }

    /**
     * ユーザー登録処理を実行するためのメソッド
     * 登録画面のフォームが送信（POST）された時に呼ばれる
     */
    @PostMapping("/register")
    public String registerUser(UserForm userForm) {
        // 1. UserForm（画面からのデータ）をUserEntity（DB保存用オブジェクト）にコピー
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userForm, userEntity);

        // 2. UserServiceを呼び出して、ユーザー登録処理を依頼
        userService.registerUser(userEntity);

        // 3. 登録完了後、ログイン画面にリダイレクト（移動）させる
        return "redirect:/login";
    }
}