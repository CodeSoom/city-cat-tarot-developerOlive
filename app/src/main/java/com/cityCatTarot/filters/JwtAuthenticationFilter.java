package com.cityCatTarot.filters;

import com.cityCatTarot.application.AuthenticationService;
import com.cityCatTarot.domain.Role;
import com.cityCatTarot.security.UserAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Jwt가 유효한 토큰인지 인증하기 위한 필터
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private final AuthenticationService authenticationService;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            AuthenticationService authenticationService) {
        super(authenticationManager);
        this.authenticationService = authenticationService;
    }

    /**
     * 주어진 요청으로 유저 인증을 수행합니다.
     *
     * @param request  요청 정보
     * @param response 응답 정보
     * @param chain    필터 체인
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        if(filterWithPathAndMethod(request)){
            chain.doFilter(request, response);
            return;
        }

        // 헤더에서 jwt를 받아온다.
        String authorization = request.getHeader("Authorization");

        if (authorization != null) {

            String accessToken = authorization.substring("Bearer ".length());

            Long userId = authenticationService.parseToken(accessToken);

            List<Role> roles = authenticationService.roles(userId);
            Authentication authentication =
                    new UserAuthentication(userId, roles);

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean filterWithPathAndMethod(HttpServletRequest request){
        String path = request.getRequestURI();
        if (path.startsWith("/tarotChat")){
            return true;
        }
        return false;
    }
}

