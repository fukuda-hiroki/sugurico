package jp.sugurico.sugurico.controller;

import jp.sugurico.sugurico.entity.ForumEntity;
import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.service.ForumService;
import jp.sugurico.sugurico.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")   //  このクラスは/postで始まるURLを処理
public class ForumController {
    /**
     * 【画面表示処理】新規投稿ページ
     *
     * @param model HTMLにデータを渡すための入れ物
     * @return 表示するHTMLファイルのパス
     */
    @GetMapping("/new")
    public String newPost(Model model) {
        //  空のForumFormオブジェクトをHTMLに渡す
        model.addAttribute("forumForm",new ForumForm());
        return "post/new";
        //  templates/post/new.htmlを表示
    }

    @Autowired
    private ForumService forumService;

    @Autowired
    private UserService userService;    //  ログインユーザー情報を取得するために利用

    /**
     * 【データ登録処理】新規投稿フォームの送信先
     *
     * @param form フォームから送信されたデータ
     * @param userDetails ログイン中のユーザー情報
     * @return リダイレクト先のURL
     */
    @PostMapping
    public String createPost(ForumForm form,
                             @AuthenticationPrincipal UserDetails userDetails){
        ForumEntity forumEntity = new ForumEntity();
        BeanUtils.copyProperties(form, forumEntity);

        //  ログイン中のユーザー処理情報を取得
        UserEntity loginUser = userService.findByLoginId(userDetails.getUsername()).orElseThrow();

        //  Serviceを呼び出して投稿処理を依頼
        forumService.createPost(forumEntity,loginUser);

        return "redirect:/";    //  投稿後はトップページにリダイレクト
    }


}
