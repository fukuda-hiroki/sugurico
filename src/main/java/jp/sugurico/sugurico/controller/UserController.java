package jp.sugurico.sugurico.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // ★このimportを追加
import java.util.Optional;

@Controller // このクラスがWebリクエストを処理するControllerであることを示す
public class UserController {

    @Autowired // UserServiceのインスタンスを自動で注入
    private UserService userService;

    /**
     * 【画面表示処理】ユーザー登録ページ
     *
     * GETリクエストで /register にアクセスされた時に呼ばれます。
     * ★もしユーザーが既にログイン済みであれば、トップページ("/")にリダイレクトします。
     *
     * @param request HTTPリクエスト情報を持つオブジェクト
     * @return 表示するHTMLファイルのパス、またはリダイレクト先のURL
     */
    @GetMapping("/register")
    public String showRegistrationForm(HttpServletRequest request) {
        // 現在のリクエストから、ログインしているユーザーのプリンシパル（識別情報）を取得
        if (request.getUserPrincipal() != null) {
            // ログインしている場合は、トップページにリダイレクト
            return "redirect:/";
        }
        // ログインしていない場合は、通常通り登録ページを表示
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
    public String registerUser(UserForm userForm, RedirectAttributes redirectAttributes) { // ★引数にRedirectAttributesを追加
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userForm, userEntity);

        // UserServiceを呼び出し、結果（エラーメッセージ or null）を受け取る
        String errorMessage = userService.registerUser(userEntity);

        // もしerrorMessageがnullでなければ（＝エラーがあった場合）
        if (errorMessage != null) {
            // リダイレクト先にエラーメッセージと入力値を渡す
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            redirectAttributes.addFlashAttribute("userForm", userForm);

            // 登録ページにリダイレクトして、エラーメッセージを表示させる
            return "redirect:/register";
        }

        // 成功した場合は、登録完了メッセージを渡してログイン画面にリダイレクト
        redirectAttributes.addFlashAttribute("successMessage", "ユーザー登録が完了しました。");
        return "redirect:/login";
    }

    /**
     * 【画面表示処理】ログインページ
     *
     * GETリクエストで /login にアクセスされた時に呼ばれます。
     * ★もしユーザーが既にログイン済みであれば、トップページ("/")にリダイレクトします。
     *
     * @param request HTTPリクエスト情報を持つオブジェクト
     * @return 表示するHTMLファイルのパス、またはリダイレクト先のURL
     */
    @GetMapping("/login")
    public String showLoginForm(HttpServletRequest request) {
        // 現在のリクエストから、ログインしているユーザーのプリンシパル（識別情報）を取得
        if (request.getUserPrincipal() != null) {
            // ログインしている場合は、トップページにリダイレクト
            return "redirect:/";
        }
        // ログインしていない場合は、通常通りログインページを表示
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
        Optional<UserEntity> userOptional = userService.findByLoginId(userDetails.getUsername());

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            UserForm userForm = new UserForm();
            BeanUtils.copyProperties(user, userForm);
            userForm.setPassword("");

            model.addAttribute("userForm", userForm);
            return "user/edit";
        }
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
        return "redirect:/user/edit?complete";
    }

    /**
     * 【データ削除処理】退会処理
     *
     * 処理内容：
     * 1. 現在ログインしているユーザーのログインIDを取得します。
     * 2. UserServiceを呼び出して、退会処理（論理削除）を依頼します。
     * 3. ★現在のセッションを無効化（ログアウト状態に）します。
     * 4. ★処理完了後、ログインページにリダイレクトします。
     *
     * @param userDetails Spring Securityが自動で渡してくれる、現在ログイン中のユーザー情報
     * @param request     HTTPリクエスト情報を持つオブジェクト
     * @return 処理完了後のリダイレクト先URL
     */
    @PostMapping("/user/delete")
    public String deleteUser(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        String loginId = userDetails.getUsername();
        userService.deleteUser(loginId);

        // 現在のセッションを取得して無効化する
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // ログインページにリダイレクト
        return "redirect:/login?logout";
    }
}