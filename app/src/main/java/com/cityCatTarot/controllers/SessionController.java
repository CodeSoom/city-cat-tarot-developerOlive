package com.cityCatTarot.controllers;

import com.cityCatTarot.application.AuthenticationService;
import com.cityCatTarot.dto.SessionRequestData;
import com.cityCatTarot.dto.SessionResponseData;
import com.cityCatTarot.errors.LoginFailException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 인증과 관련된 HTTP 요청을 처리합니다.
 */
@RestController
@RequestMapping("/session")
@CrossOrigin
public class SessionController {

    private AuthenticationService authenticationService;

    public SessionController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 전달된 회원 정보로 로그인하여 얻은 세션 정보를 응답합니다.
     *
     * @param sessionRequestData 회원 로그인 정보
     * @return 회원 세션 정보
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionResponseData login(
            @RequestBody SessionRequestData sessionRequestData
    ) throws LoginFailException {

        String email = sessionRequestData.getEmail();
        String password = sessionRequestData.getPassword();

        try {
            String accessToken = authenticationService.login(email, password);

            Long userId = authenticationService.parseToken(accessToken);

            return SessionResponseData.builder()
                    .accessToken(accessToken)
                    .userId(userId)
                    .build();

        } catch (LoginFailException e) {
            return null;
        }
    }
}
