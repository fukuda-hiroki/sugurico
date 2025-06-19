package jp.sugurico.sugurico.service;

// ↓↓↓↓ ここからが必要なimport文 ↓↓↓↓
import jp.sugurico.sugurico.controller.UserForm;
import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
// ↑↑↑↑ ここまでが必要なimport文 ↑↑↑↑

@Service // このクラスがビジネスロジックを担当するServiceクラスであることを示す
public class UserService {

    // @Autowiredアノテーションで、Springが自動的にUserRepositoryのインスタンスを注入してくれる
    @Autowired
    private UserRepository userRepository;

    // ★PasswordEncoderを注入するためのフィールドを追加
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ユーザー情報をデータベースに登録するメソッド
    @Transactional // このメソッド内の処理はすべて成功するか、すべて失敗するかのどちらかになる（データの整合性を保つ）
    public void registerUser(UserEntity userEntity) {
        // 登録日と更新日に現在時刻を設定
        LocalDateTime now = LocalDateTime.now();
        userEntity.setDateCreate(now);
        userEntity.setDateUpdate(now);

        // ★パスワードをハッシュ化して設定する処理を追加
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // UserRepositoryを使って、データベースにユーザー情報を保存する
        userRepository.save(userEntity);
    }

    /**
     * 【データ取得処理】ログインIDによるユーザー検索
     *
     * 処理内容：
     * Controllerから受け取ったログインIDを元に、UserRepositoryを呼び出して
     * データベースから該当するユーザーを検索します。
     *
     * @param loginId 検索するログインID
     * @return 見つかったユーザー情報 (見つからない場合は空)
     */
    public Optional<UserEntity> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    /**
     * 【データ更新処理】ユーザー情報の更新
     *
     * 処理内容：
     * 1. ログインIDを元に、データベースから更新対象のユーザー情報を取得します。
     * 2. 取得したユーザー情報(Entity)を、フォームから送られてきた内容で上書きします。
     * 3. パスワードが入力されていれば、ハッシュ化して上書きします。
     * 4. 更新日時を現在時刻に設定します。
     * 5. UserRepositoryを呼び出して、変更をデータベースに保存(UPDATE)します。
     *
     * @param loginId  更新対象のユーザーのログインID
     * @param userForm 画面フォームから送られてきたデータ
     */
    @Transactional
    public void updateUser(String loginId, UserForm userForm) {
        // ログインIDで現在のユーザー情報をDBから取得
        UserEntity userEntity = userRepository.findByLoginId(loginId).orElseThrow();

        // フォームから入力された値で上書き
        userEntity.setName(userForm.getName());
        userEntity.setUserName(userForm.getUserName());
        userEntity.setMail(userForm.getMail());

        // パスワードが入力されている場合のみ、ハッシュ化して上書き
        if (userForm.getPassword() != null && !userForm.getPassword().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(userForm.getPassword()));
        }

        // 更新日を設定
        userEntity.setDateUpdate(LocalDateTime.now());

        // データベースに保存（IDが同じなので、UPDATE文が実行される）
        userRepository.save(userEntity);
    }

    // jp/sugurico/sugurico/service/UserService.java に追加

// ...

    /**
     * 【データ更新処理】ユーザーの退会（論理削除）
     *
     * 処理内容：
     * 1. ログインIDを元に、データベースから更新対象のユーザー情報を取得します。
     * 2. ユーザーの退会フラグ(withdrawal_flag)をtrueに設定します。
     * 3. 更新日時を現在時刻に設定します。
     * 4. 変更をデータベースに保存します。
     *
     * @param loginId 退会するユーザーのログインID
     */
    @Transactional
    public void deleteUser(String loginId) {
        // DBから現在の情報を取得
        UserEntity userEntity = userRepository.findByLoginId(loginId).orElseThrow();

        // 退会フラグを立てる
        userEntity.setWithdrawalFlag(true);
        userEntity.setDateUpdate(LocalDateTime.now());

        // データベースに保存
        userRepository.save(userEntity);
    }
}