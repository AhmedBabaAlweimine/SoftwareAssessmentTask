package com.alweimine.supportticketsystem.repositories;

import com.alweimine.supportticketsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUsernameAndPasswordAndRole(String username, String password,User.Role role);
    int deleteByUserId(Long userId);

}
