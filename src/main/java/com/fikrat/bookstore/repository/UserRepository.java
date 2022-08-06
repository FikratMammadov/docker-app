package com.fikrat.bookstore.repository;

import com.fikrat.bookstore.model.Role;
import com.fikrat.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(int id);
    Boolean existsByUsername(String username);
}
