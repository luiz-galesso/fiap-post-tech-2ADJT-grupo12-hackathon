package com.hackathon.fiap.msautenticacao.infrastructure.user.repository;

import com.hackathon.fiap.msautenticacao.entity.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByUsername(String username);

}
