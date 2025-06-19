package jp.sugurico.sugurico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // このクラスが設定用クラスであることを示す
@EnableWebSecurity // Webセキュリティを有効にする
public class SecurityConfig {

    /**
     * パスワードをハッシュ化するための仕組み（PasswordEncoder）をBeanとして登録
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * セキュリティに関する設定を定義するメソッド
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 認可（どのページに誰がアクセスできるか）の設定
                .authorizeHttpRequests(authorize -> authorize
                        // "/register" と "/css/**", "/js/**" などの静的リソースは、誰でもアクセス可能
                        .requestMatchers("/register", "/css/**", "/js/**").permitAll()
                        // それ以外のリクエストは、すべて認証（ログイン）が必要
                        .anyRequest().authenticated()
                )
                // ログインに関する設定
                .formLogin(login -> login
                        // ログイン処理を行うURLを指定
                        .loginProcessingUrl("/login")
                        // ログインページのURLを指定
                        .loginPage("/login")
                        // ログインに成功した場合のリダイレクト先
                        .defaultSuccessUrl("/", true)
                        // ログインに失敗した場合のリダイレクト先
                        .failureUrl("/login?error")
                        // ログインページのアクセスは誰でも許可
                        .permitAll()
                )
                // ログアウトに関する設定
                .logout(logout -> logout
                        // ログアウトページのURLを指定
                        .logoutUrl("/logout")
                        // ログアウトに成功した場合のリダイレクト先
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }
}