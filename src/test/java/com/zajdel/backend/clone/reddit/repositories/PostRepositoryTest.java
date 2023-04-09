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
    public void itShouldFindAllPostsByAuthorId() {
        // given
        Long authorId = author1.getId();
        int expectedSize = 3;

        // when
        List<Post> expected = underTest.findAllPostsByAuthorId(authorId);

        // then
        assertThat(expected.size()).isEqualTo(expectedSize);
    }

    @Test
    public void itShouldReturnEmptyListOfPostsWhenAuthorDoesNotHavePosts() {

        // given
        Long authorId = author2.getId();

        // when
        List<Post> expected = underTest.findAllPostsByAuthorId(authorId);

        // then
        assertThat(expected.size()).isZero();
    }

    @Test
    public void itShouldReturnEmptyListOfPostsWhenAuthorDoesNotExists() {

        // given
        Long authorId = 100L;
        int expectedSize = 0;

        // when
        List<Post> expected = underTest.findAllPostsByAuthorId(authorId);

        // then
        assertThat(expected.size()).isEqualTo(expectedSize);
    }


    @Test
    public void ItShouldFindPostByIdAndAuthorId() {
        // given
        Long authorId = author1.getId();
        Long postId = post.getId();

        // when
        Optional<Post> expected = underTest.findPostByIdAndAuthorId(postId, authorId);

        // then
        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get()).isEqualTo(post);

    }

    @Test
    public void ItShouldNotFindPostByIdAndAuthorIdWhenPostIsNotCreatedByThatUser() {
        //given
        Long authorId = author2.getId();
        Long postId = post.getId();

        //when
        Optional<Post> expected = underTest.findPostByIdAndAuthorId(postId, authorId);

        //then
        assertThat(expected.isPresent()).isFalse();
    }

    @Test
    public void ItShouldNotFindPostByIdAndAuthorIdWhenAuthorDoesNotExists() {
        //given
        Long authorId = 100L;
        Long postId = post.getId();

        //when
        Optional<Post> expected = underTest.findPostByIdAndAuthorId(postId, authorId);

        //then
        assertThat(expected.isPresent()).isFalse();

    }

    @Test
    public void ItShouldDeletePostByIdAndAuthorId() {
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