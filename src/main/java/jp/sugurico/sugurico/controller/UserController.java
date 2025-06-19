package jp.sugurico.sugurico.controller;

// ↓↓↓↓ ここからが必要なimport文 ↓↓↓↓
import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Optional; // ★この行を追加
// ↑↑↑↑ ここまでが必要なimport文 ↑↑↑↑

@Controller // このクラスがWebリクエストを処理するControllerであることを示す
public class UserController {

    @Autowired // UserServiceのインスタンスを自動で注入
    private UserService userService;

    /**
     * 【画面表示処理】ユーザー登録ページ
     *
     * GETリクエストで /register にアクセスされた時に呼ばれます。
     * "user/register.html" を画面に表示します。
     *
     * @return 表示するHTMLファイルのパス
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "user/register";
    }

    /**
     * 【データ登録処理】ユーザー登録フォームの送信先
     *
     * 登録画面のフォームが送信（POST）された時に呼ばれます。
     *
     * @param userForm 画面フォームから送信されたデータが自動で入るオブジェクト
     * @return 処理完了後のリダイレクト先URL
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

    /**
     * 【画面表示処理】ログインページ
     *
     * GETリクエストで /login にアクセスされた時に呼ばれます。
     * "user/login.html" を画面に表示します。
     *
     * @return 表示するHTMLファイルのパス
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    /**
     * 【画面表示処理】トップページ
     *
     * GETリクエストで / にアクセスされた時に呼ばれます。
     * "index.html" を画面に表示します。
     *
     * @return 表示するHTMLファイルのパス
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 【画面表示処理】登録情報変更ページ
     *
     * GETリクエストで /user/edit にアクセスされた時に呼ばれます。
     *
     * @param userDetails Spring Securityが自動で渡してくれる、現在ログイン中のユーザー情報
     * @param model       ControllerからHTMLへデータを渡すための入れ物
     * @return 表示するHTMLファイルのパス、またはリダイレクト先URL
     */
    @GetMapping("/user/edit")
    public String showEditForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        // 現在ログインしているユーザーの情報を取得
        Optional<UserEntity> userOptional = userService.findByLoginId(userDetails.getUsername());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            // 画面のフォームに表示するために、EntityからFormにデータをコピー
            UserForm userForm = new UserForm();
            BeanUtils.copyProperties(user, userForm);
            // パスワードは表示しないので空にする
            userForm.setPassword("");

            model.addAttribute("userForm", userForm);
            return "user/edit"; // "user/edit.html" を表示
        }
        // ユーザーが見つからない場合はトップページにリダイレクト
        return "redirect:/";
    }

    /**
     * 【データ更新処理】登録情報変更フォームの送信先
     *
     * 編集画面のフォームが送信（POST）された時に呼ばれます。
     *
     * @param userDetails Spring Securityが自動で渡してくれる、現在ログイン中のユーザー情報
     * @param userForm    画面フォームから送信されたデータが自動で入るオブジェクト
     * @return 処理完了後のリダイレクト先URL
     */
    @PostMapping("/user/edit")
    public String updateUser(@AuthenticationPrincipal UserDetails userDetails, UserForm userForm) {
        String loginId = userDetails.getUsername();
        userService.updateUser(loginId, userForm);
        return "redirect:/user/edit?complete"; // 更新完了後は再度編集画面にリダイレクト
    }
}