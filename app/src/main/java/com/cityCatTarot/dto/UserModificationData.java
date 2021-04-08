package com.cityCatTarot.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModificationData {
    @NotBlank
    @Mapping("nickName")
    private String nickName;

    @NotBlank
    @Mapping("password")
    private String password;
}
