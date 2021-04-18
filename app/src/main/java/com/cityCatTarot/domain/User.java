package com.cityCatTarot.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

/**
 * 회원 정보.
 */
@Entity(name="User")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotEmpty(message = "회원 이메일은 필수 입니다.")
    @Column(name = "user_email")
    private String email;

    @NotEmpty(message = "회원 닉네임은 필수 입니다.")
    @Column(name = "user_nick_name")
    private String nickName;

    @NotEmpty(message = "회원 비밀번호는 필수 입니다.")
    @Column(name = "user_password")
    private String password;

    @Builder.Default
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * 회원의 정보를 변경합니다.
     * @param source 변경할 사용자 정보
     */
    public void changeWith(User source) {

        if(source.getNickName() != null && source.getPassword() != null){
            nickName = source.nickName;
            password = source.password;
            return;
        }

        if(source.getNickName() != null){
            nickName = source.nickName;
            return;
        }

        if(source.getPassword() != null){
            password = source.password;
            return;
        }
    }

    /**
     * 회원의 비밀번호를 암호화합니다.
     * @param password 암호화 되지 않은 회원의 비밀번호
     * @param passwordEncoder 패스워드 인코더
     */
    public void changePassword(String password,
                               PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     * 회원을 삭제합니다.
     */
    public void destroy() {
        deleted = true;
    }

    /**
     * 회원이 삭제되지 않았고, 비밀번호가 일치하면 true를 반환합니다.
     *
     * @param password 비밀번호
     * @param passwordEncoder 패스워드 인코더
     */
    public boolean authenticate(String password,
                                PasswordEncoder passwordEncoder) {
        return !deleted && passwordEncoder.matches(password, this.password);
    }
}
