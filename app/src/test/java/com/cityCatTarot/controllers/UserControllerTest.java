package com.cityCatTarot.controllers;

import com.cityCatTarot.application.AuthenticationService;
import com.cityCatTarot.application.UserService;
import com.cityCatTarot.domain.Role;
import com.cityCatTarot.domain.User;
import com.cityCatTarot.dto.UserModificationData;
import com.cityCatTarot.dto.UserRegistrationData;
import com.cityCatTarot.errors.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String MY_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.uGy7d5cSkGfI6SR9rHrbwb6ebZW9gs3DOEOAoVKl9RM";

    private static final String OTHER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.uGy7d5cSkGfI6SR9rHrbwb6ebZW9gs3DOEOAoVKl911";

    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJ1c2VySWQiOjEwMDR9.3GV5ZH3flBf0cnaXQCNNZlT4mgyFyBUhn3LKzQohh1A";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {

        given(userService.registerUser(any(UserRegistrationData.class)))
                .will(invocation -> {
                    UserRegistrationData registrationData = invocation.getArgument(0);
                    return User.builder()
                            .id(1L)
                            .email(registrationData.getEmail())
                            .nickName(registrationData.getNickName())
                            .build();
                });

        given(
                userService.updateUser(
                        eq(1L),
                        any(UserModificationData.class),
                        eq(1L)
                )
        )
                .will(invocation -> {
                    Long id = invocation.getArgument(0);
                    UserModificationData modificationData =
                            invocation.getArgument(1);
                    return User.builder()
                            .id(id)
                            .email("tester@example.com")
                            .nickName(modificationData.getNickName())
                            .build();
                });

        given(
                userService.updateUser(
                        eq(100L),
                        any(UserModificationData.class),
                        eq(1L)
                )
        )
                .willThrow(new UserNotFoundException(100L));

        given(
                userService.updateUser(
                        eq(1L),
                        any(UserModificationData.class),
                        eq(2L)
                )
        )
                .willThrow(new AccessDeniedException("Access denied"));

        given(userService.deleteUser(100L))
                .willThrow(new UserNotFoundException(100L));

        given(authenticationService.parseToken(MY_TOKEN)).willReturn(1L);
        given(authenticationService.parseToken(OTHER_TOKEN)).willReturn(2L);
        given(authenticationService.parseToken(ADMIN_TOKEN)).willReturn(100L);

        given(authenticationService.roles(1L))
                .willReturn(Arrays.asList(new Role("USER")));
        given(authenticationService.roles(2L))
                .willReturn(Arrays.asList(new Role("USER")));
        given(authenticationService.roles(100L))
                .willReturn(Arrays.asList(new Role("USER")));
    }

    @Test
    @DisplayName("POST 요청은 새로운 회원을 추가하면 201 코드와 생성된 회원을 응답한다.")
    void registerUserWithValidAttributes() throws Exception {
        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"tester@example.com\"," +
                                "\"nickName\":\"Tester\",\"password\":\"test\"}")
        )
                .andExpect(status().isCreated())
                .andExpect(content().string(
                        containsString("\"id\":1")
                ))
                .andExpect(content().string(
                        containsString("\"email\":\"tester@example.com\"")
                ))
                .andExpect(content().string(
                        containsString("\"nickName\":\"Tester\"")
                ));

        verify(userService).registerUser(any(UserRegistrationData.class));
    }

    @DisplayName("POST 요청은 새로운 회원정보가 없는 상태로 가입하려고 하면 404 에러를 던진다.")
    @Test
    void registerUserWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH 요청은 로그인 된 상태이고, 유효한 수정 정보가 주어지면 200 코드와 수정된 회원을 응답한다.")
    void updateUserWithValidAttributes() throws Exception {
        mockMvc.perform(
                patch("/patch-userInfo/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickName\":\"revisedNickName\",\"password\":\"revisedPassword\"}")
                        .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1")
                ))
                .andExpect(content().string(
                        containsString("\"nickName\":\"revisedNickName\"")
                ));

        verify(userService)
                .updateUser(eq(1L), any(UserModificationData.class), eq(1L));
    }

    @Test
    @DisplayName("PATCH 요청은 유효하지 않은 정보가 주어지면 400 에러를 던진다.")
    void updateUserWithInvalidAttributes() throws Exception {
        mockMvc.perform(
                patch("/patch-userInfo/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH 요청은 등록되지 않은 회원의 id가 주어지면 404 코드를 응답한다.")
    void updateUserWithNotExistingId() throws Exception {
        mockMvc.perform(
                patch("/patch-userInfo/{id}", 100L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickName\":\"revisedNickName\",\"password\":\"revisedPassword\"}")
                        .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isNotFound());

        verify(userService).updateUser(
                eq(100L),
                any(UserModificationData.class),
                eq(1L));
    }

    @Test
    @DisplayName("PATCH 요청은 엑세스 토큰이 없으며 401 에러를 응답한다.")
    void updateUserWithoutAccessToken() throws Exception {
        mockMvc.perform(
                patch("/patch-userInfo/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickName\":\"TEST\",\"password\":\"test\"}")
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("PATCH 요청은 엑세스 토큰이 유효하지 않으면 403 에러를 응답한다.")
    void updateUserWithOthersAccessToken() throws Exception {
        mockMvc.perform(
                patch("/patch-userInfo/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickName\":\"TEST\",\"password\":\"test\"}")
                        .header("Authorization", "Bearer " + OTHER_TOKEN)
        )
                .andExpect(status().isForbidden());

        verify(userService)
                .updateUser(eq(1L), any(UserModificationData.class), eq(2L));
    }

    @Test
    @DisplayName("DELETE 요청은 등록된 회원의 id가 주어지면 그 id에 해당하는 회원을 삭제한다.")
    void destroyWithExistingId() throws Exception {
        mockMvc.perform(delete("/delete-user/{id}",1L)
                .header("Authorization", "Bearer " + MY_TOKEN)
        )
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @DisplayName("DELETE 요청은 등록되지 않은 회원의 id가 주어지면 404 코드를 응답한다.")
    void destroyWithNotExistingId() throws Exception {
        mockMvc.perform(delete("/delete-user/{id}",100L)
                .header("Authorization", "Bearer " + MY_TOKEN))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(100L);
    }

    @Test
    @DisplayName("DELETE 요청은 엑세스 토큰이 없으면 401 에러를 응답한다.")
    void destroyWithoutAccessToken() throws Exception {
        mockMvc.perform(delete("/delete-user/{id}",1L))
                .andExpect(status().isUnauthorized());
    }
}
