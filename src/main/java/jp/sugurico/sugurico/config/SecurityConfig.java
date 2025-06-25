package jp.sugurico.sugurico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

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
                                // ▼▼▼ ここからが修正・追記部分 ▼▼▼

                                // 【誰でもアクセス可能（ログイン不要）なページ】
                                // 静的リソース（CSS, JavaScriptなど）
                                .requestMatchers("/css/**", "/js/**").permitAll()
                                // トップページ、ログインページ、ユーザー登録ページ
                                .requestMatchers("/", "/login", "/register").permitAll()
                                // 投稿一覧ページ、投稿詳細ページ (例: /posts, /posts/1)
                                // ※注意：正規表現を使い、数字のIDを持つパスを指定
                                .requestMatchers(HttpMethod.GET, "/posts", "/posts/{id:\\d+}").permitAll()

                                // 【ログインしているユーザーのみアクセス可能なページ】
                                // これら以外のすべてのリクエストは認証（ログイン）が必要
                                .anyRequest().authenticated()

                        // ▲▲▲ ここまでが修正・追記部分 ▲▲▲
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
                        // ログインページのアクセスは誰でも許可（ただし上記のanonymous()が優先される）
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