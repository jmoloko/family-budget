package com.example.familybudget.repository;

import com.example.familybudget.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity getByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT u.id FROM UserEntity as u WHERE u.email = ?1")
    Long getIdByEmail(String email);

    UserEntity findByActivationCode(String code);
}
