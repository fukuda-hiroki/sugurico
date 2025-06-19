package jp.sugurico.sugurico.service;

import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // ★インポートを追加
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ★PasswordEncoderを注入するためのフィールドを追加
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserEntity userEntity) {
        LocalDateTime now = LocalDateTime.now();
        userEntity.setDateCreate(now);
        userEntity.setDateUpdate(now);

        // ★パスワードをハッシュ化して設定する処理を追加
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        userRepository.save(userEntity);
    }
}