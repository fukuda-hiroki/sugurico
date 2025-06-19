package jp.sugurico.sugurico.entity; // パッケージ名が正しいか確認

import jakarta.persistence.*; // JPA関連のアノテーションをインポート
import lombok.Data; // Lombokをインポート
import java.time.LocalDateTime; // 日時を扱うクラスをインポート

/**
 * ユーザー情報を管理するEntityクラス (usersテーブルに対応)
 */
@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructorをまとめて生成
@Entity // このクラスがJPAの管理対象（エンティティ）であることを示す
@Table(name = "users") // どのデータベーステーブルに対応するかを明記
public class UserEntity {

    // 主キーの設定
    @Id // このフィールドがテーブルの主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主キーの値をDBが自動生成することを示す (BIGSERIALに対応)
    @Column(name = "user_id") // DBのカラム名とJavaのフィールド名を紐付け
    private Long userId;

    // その他のカラムの設定
    @Column(name = "name", nullable = false) // "name"カラムに対応、NOT NULL制約
    private String name;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "login_id", nullable = false, unique = true) // UNIQUE制約
    private String loginId;

    @Column(name = "mail", nullable = false, unique = true)
    private String mail;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "date_create", nullable = false)
    private LocalDateTime dateCreate;

    @Column(name = "date_update", nullable = false)
    private LocalDateTime dateUpdate;

    @Column(name = "premium_flag", nullable = false)
    private boolean premiumFlag;

    @Column(name = "withdrawal_flag", nullable = false)
    private boolean withdrawalFlag;
}