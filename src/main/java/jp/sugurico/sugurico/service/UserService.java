package jp.sugurico.sugurico.service;

import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service // このクラスがビジネスロジックを担当するServiceクラスであることを示す
public class UserService {

    // @Autowiredアノテーションで、Springが自動的にUserRepositoryのインスタンスを注入してくれる
    @Autowired
    private UserRepository userRepository;

    // ユーザー情報をデータベースに登録するメソッド
    @Transactional // このメソッド内の処理はすべて成功するか、すべて失敗するかのどちらかになる（データの整合性を保つ）
    public void registerUser(UserEntity userEntity) {
        // 登録日と更新日に現在時刻を設定
        LocalDateTime now = LocalDateTime.now();
        userEntity.setDateCreate(now);
        userEntity.setDateUpdate(now);

        // パスワードをハッシュ化する処理（今は省略。後で追加します）
        // userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // UserRepositoryを使って、データベースにユーザー情報を保存する
        userRepository.save(userEntity);
    }
}