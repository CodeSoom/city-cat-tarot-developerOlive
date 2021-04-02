package com.cityCatTarot.controllers;

import com.cityCatTarot.application.UserService;
import com.cityCatTarot.domain.User;
import com.cityCatTarot.dto.UserModificationData;
import com.cityCatTarot.dto.UserRegistrationData;
import com.cityCatTarot.dto.UserResultData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * 회원에 대한 HTTP 요청 처리를 담당합니다.
 */
@RestController
@CrossOrigin
@RequestMapping(produces = "application/json; charset=UTF8")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 전체 회원을 리턴합니다.
     */
    @GetMapping(produces = "application/json; charset=UTF8")
    public List<User> list() {
        return userService.getUsers();
    }

    /**
     * 전달된 회원 정보로 회원을 생성한 뒤, 그 회원을 리턴합니다.
     * @param registrationData 회원 정보
     * @return 생성된 회원
     */
    @PostMapping(value="/register", produces = "application/json; charset=UTF8")
    @ResponseStatus(HttpStatus.CREATED)
    UserResultData create(@RequestBody @Valid UserRegistrationData registrationData) {
        User user = userService.registerUser(registrationData);
        return getUserResultData(user);
    }

    /**
     * 전달된 식별자에 해당하는 회원을 찾고, 함께 주어진 회원 정보로 수정한 후 리턴합니다.
     *
     * @param id                회원 식별자
     * @param modificationData  수정할 회원 정보
     * @return 수정된 회원
     */
    @PatchMapping(value="patch-userInfo/{id}", produces = "application/json; charset=UTF8")
    UserResultData update(
            @PathVariable Long id,
            @RequestBody @Valid UserModificationData modificationData
    ) {
        User user = userService.updateUser(id, modificationData);
        return getUserResultData(user);
    }

    /**
     * 전달된 식별자에 해당하는 회원을 삭제합니다.
     *
     * @param id 회원 식별자
     */
    @DeleteMapping("delete-user/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    private UserResultData getUserResultData(User user) {
        return UserResultData.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickName(user.getNickName())
                .build();
    }
}
