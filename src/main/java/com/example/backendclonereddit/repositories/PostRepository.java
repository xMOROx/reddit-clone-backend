package com.example.backendclonereddit.repositories;

import com.example.backendclonereddit.configs.ApiPaths;
import com.example.backendclonereddit.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {




}
