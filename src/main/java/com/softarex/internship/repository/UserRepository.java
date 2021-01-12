package com.softarex.internship.repository;

import com.softarex.internship.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);
    User getByEmail(String email);
}
