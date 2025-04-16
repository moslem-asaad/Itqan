package com.example.itqan.repository;

import com.example.itqan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);

    @Query("SELECT u.userName FROM User u WHERE u.userName IN :userNames")
    List<String> findUserNamesByUserNameIn(@Param("userNames") Collection<String> userNames);



}
