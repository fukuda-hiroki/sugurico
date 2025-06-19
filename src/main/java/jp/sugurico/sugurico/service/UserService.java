package jp.sugurico.sugurico.service;

import jp.sugurico.sugurico.controller.UserForm;
import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 【データ登録処理】ユーザー情報の登録
     *
     * @param userEntity 登録するユーザー情報
     * @return 登録に成功した場合はnull、重複エラーがあった場合はエラーメッセージの文字列
     */
    @Transactional
    public String registerUser(UserEntity userEntity) { // ★戻り値を void から String に変更
        // 重複チェック
        if (userRepository.existsByLoginId(userEntity.getLoginId())) {
            return "このログインIDは既に使用されています。";
        }
        if (userRepository.existsByMail(userEntity.getMail())) {
            return "このメールアドレスは既に使用されています。";
        }

        // 登録処理
        LocalDateTime now = LocalDateTime.now();
        userEntity.setDateCreate(now);
        userEntity.setDateUpdate(now);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);

        return null; // 成功した場合はnullを返す
    }

    /**
     * 【データ取得処理】ログインIDによるユーザー検索
     * (中略)
     */
    public Optional<UserEntity> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    /**
     * 【データ更新処理】ユーザー情報の更新
     * (中略)
     */
    @Transactional
    public void updateUser(String loginId, UserForm userForm) {
        UserEntity userEntity = userRepository.findByLoginId(loginId).orElseThrow();
        userEntity.setName(userForm.getName());
        userEntity.setUserName(userForm.getUserName());
        userEntity.setMail(userForm.getMail());
        if (userForm.getPassword() != null && !userForm.getPassword().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(userForm.getPassword()));
        }
        userEntity.setDateUpdate(LocalDateTime.now());
        userRepository.save(userEntity);
    }

    /**
     * 【データ更新処理】ユーザーの退会（論理削除）
     * (中略)
     */
    @Transactional
    public void deleteUser(String loginId) {
        UserEntity userEntity = userRepository.findByLoginId(loginId).orElseThrow();
        userEntity.setWithdrawalFlag(true);
        userEntity.setDateUpdate(LocalDateTime.now());
        userRepository.save(userEntity);
    }
}