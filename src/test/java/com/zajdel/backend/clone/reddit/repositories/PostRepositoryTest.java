package com.zajdel.backend.clone.reddit.repositories;

import com.zajdel.backend.clone.reddit.entities.Post;
import com.zajdel.backend.clone.reddit.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository underTest;

    @Autowired
    private UserRepository userRepository;

    Post post;
    Post post2;
    Post post3;
    User author1;
    User author2;

    @BeforeEach
    void setUp() {
        author1 = new User(
                "username1",
                "password1",
                "email1@gmail.com",
                null,
                null,
                null,
                null,
                null,
                null);

        author2 = new User(
                "username2",
                "password2",
                "email2@gmail.com",
                null,
                null,
                null,
                null,
                null,
                null);

        post = new Post(
                null,
                "title1",
                "description1dasdsa",
                null,
                null,
                null,
                null,
                LocalDateTime.now().minusMinutes(10),
                null
        );

        post2 = new Post(
                null,
                "title2",
                "description2dadada",
                null,
                null,
                null,
                null,
                LocalDateTime.now().minusMinutes(10),
                null
        );

        post3 = new Post(
                null,
                "title3",
                "Description3Descr",
                null,
                null,
                null,
                null,
                LocalDateTime.now().minusMinutes(10),
                null
        );

        List<Post> posts = List.of(post, post2, post3);
        author1.setPosts(posts);

        underTest.saveAll(posts);

        posts.forEach(post -> {
            post.setAuthor(author1);
        });

        List<User> authors = List.of(author1, author2);
        userRepository.saveAll(authors);

    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void itShouldFindAllPostsByAuthorId() {
        // given
        Long authorId = author1.getId();
        Long authorId2 = author2.getId();
        Long authorId3 = 100L;
        int expectedSize = 3;

        // when
        List<Post> expected = underTest.findAllPostsByAuthorId(authorId);
        List<Post> expected2 = underTest.findAllPostsByAuthorId(authorId2);
        List<Post> expected3 = underTest.findAllPostsByAuthorId(authorId3);

        // then
        assertThat(expected.size()).isEqualTo(expectedSize);
        assertThat(expected2.size()).isZero();
        assertThat(expected3.size()).isZero();
    }

    @Test
    public void findPostByIdAndAuthorId() {
        // given
        Long authorId = author1.getId();
        Long authorId2 = author2.getId();
        Long authorId3 = 100L;
        Long postId = post.getId();

        // when
        Optional<Post> expected = underTest.findPostByIdAndAuthorId(postId, authorId);
        Optional<Post> expected2 = underTest.findPostByIdAndAuthorId(postId, authorId2);
        Optional<Post> expected3 = underTest.findPostByIdAndAuthorId(postId, authorId3);

        // then
        assertThat(expected.isPresent()).isTrue();
        assertThat(expected2.isPresent()).isFalse();
        assertThat(expected3.isPresent()).isFalse();

    }

    @Test
    public void deletePostByIdAndAuthorId() {
        // given
        Long authorId = author1.getId();
        Long authorId2 = author2.getId();
        Long authorId3 = 100L;
        Long postId = post.getId();
        int expectedSize = 2;

        // when
        underTest.deletePostByIdAndAuthorId(postId, authorId);
        underTest.deletePostByIdAndAuthorId(postId, authorId2);
        underTest.deletePostByIdAndAuthorId(postId, authorId3);

        // then
        assertThat(underTest.findAll().size()).isEqualTo(expectedSize);


    }
}