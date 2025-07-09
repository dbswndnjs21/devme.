package com.erp.repository;

import com.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT u.username FROM User u WHERE u.id = :id")
    String findUsernameById(@Param("id") Long id);

}
