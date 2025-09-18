package com.mehmetsadullahguven.repository;

import com.mehmetsadullahguven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    //@Query(value = "from User where username = : username")
    Optional<User> findByUsername(String username);
}
