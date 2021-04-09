package com.cityCatTarot.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 회원 생성 요청 정보.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationData {
    @NotBlank
    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("nickName")
    private String nickName;

    @NotBlank
    @Mapping("password")
    private String password;
}
