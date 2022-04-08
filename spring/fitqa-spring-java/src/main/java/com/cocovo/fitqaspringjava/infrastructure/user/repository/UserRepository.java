package com.cocovo.fitqaspringjava.infrastructure.user.repository;

import com.cocovo.fitqaspringjava.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   // 이미 email 을 통해 생선된 사용자인지 체크
    Optional<User> findByUserToken(String userToken);
}
