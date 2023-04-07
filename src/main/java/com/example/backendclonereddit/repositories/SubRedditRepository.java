package com.example.backendclonereddit.repositories;

import com.example.backendclonereddit.entities.SubReddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {
    List<SubReddit> findAllByOwnerId(Long ownerId);
}
