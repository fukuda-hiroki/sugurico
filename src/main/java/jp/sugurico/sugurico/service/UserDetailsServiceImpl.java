package jp.sugurico.sugurico.service;

import jp.sugurico.sugurico.entity.UserEntity;
import jp.sugurico.sugurico.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // ログインIDを使って、データベースからユーザー情報を検索
        Optional<UserEntity> userOptional = userRepository.findByLoginId(loginId);

        // ユーザーが見つからなかった場合、例外をスロー
        UserEntity userEntity = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));

        // Spring Securityが理解できるUserDetailsオブジェクトに変換して返す
        return User.withUsername(userEntity.getLoginId())
                .password(userEntity.getPassword()) // DBに保存されているハッシュ化済みのパスワード
                .roles("USER") // ユーザーの権限（今は全員"USER"ロール）
                .build();
    }
}