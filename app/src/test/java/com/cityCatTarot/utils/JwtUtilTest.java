package com.cityCatTarot.utils;

import com.cityCatTarot.errors.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET = "11112222333344445555666677778888";

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo1fQ.snmVKWWu4tRDy1U1zCKk3D5hbJpvuVzA89SzsZknB28";

    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxfQ.uGy7d5cSkGfI6SR9rHrbwb6ebZW9gs3DOEOAoVKl9R1";

    private static final Long USER_ID = 5L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    @DisplayName("encode는 회원 id가 주어지면 생성된 토큰을 리턴한다.")
    void encode() {
        String token = jwtUtil.encode(USER_ID);

        assertThat(token).isEqualTo(VALID_TOKEN);
    }

    @Test
    @DisplayName("decode에 유효한 토큰이 주어지면 클레임을 리턴한다.")
    void decodeWithValidToken() {
        Claims claims = jwtUtil.decode(VALID_TOKEN);

        assertThat(claims.get("user_id", Long.class)).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("decode에 유효하지 않은 토큰이 주어지면 예외를 던진다.")
    void decodeWithInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.decode(INVALID_TOKEN))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("decode에 빈 토큰이 주어지면 예외를 던진다.")
    void decodeWithEmptyToken() {
        assertThatThrownBy(() -> jwtUtil.decode(null))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode(""))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> jwtUtil.decode("   "))
                .isInstanceOf(InvalidTokenException.class);
    }
}
