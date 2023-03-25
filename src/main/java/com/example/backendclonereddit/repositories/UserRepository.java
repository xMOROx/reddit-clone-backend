package com.example.backendclonereddit.repositories;
import com.example.backendclonereddit.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
