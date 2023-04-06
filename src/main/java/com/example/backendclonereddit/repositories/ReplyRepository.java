package com.example.backendclonereddit.repositories;

import com.example.backendclonereddit.entities.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
