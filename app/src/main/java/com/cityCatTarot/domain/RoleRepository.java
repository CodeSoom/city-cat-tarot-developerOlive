package com.cityCatTarot.domain;

import java.util.List;

/**
 * 사용자 권한 저장소.
 */
public interface RoleRepository {
    List<Role> findAllByUserId(Long userId);

    Role save(Role role);
}
