package com.zajdel.backend.clone.reddit.repositories;

import com.zajdel.backend.clone.reddit.entities.Comment;
import com.zajdel.backend.clone.reddit.entities.Post;
import com.zajdel.backend.clone.reddit.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private User user2;
    private User user3;
    private Post post;
    private Post post2;
    private Comment comment;
    private Comment comment2;
    private Comment comment3;

    @BeforeEach
    void setUp() {
        user = new User(
                "username1",
                "password1",
                "email1@gmail.com",
                null,
                null,
                null,
                null,
                null,
                null);

        user2 = new User(
                "username2",
                "password2",
                "email2@gmail.com",
                null,
                null,
                null,
                null,
                null,
                null);

        user3 = new User(
                "username3",
                "password3",
                "email3@gmail.com",
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
                "description2dasdsa",
                null,
                null,
                null,
                null,
                LocalDateTime.now().minusMinutes(10),
                null
        );

        comment = new Comment(
                null,
                null,
                null,
                null,
                "content1",
                LocalDateTime.now().minusMinutes(5),
                null
        );

        comment2 = new Comment(
                null,
                null,
                null,
                null,
                "content2",
                LocalDateTime.now().minusMinutes(3),
                null
        );

        comment3 = new Comment(
                null,
                null,
                null,
                null,
                "content3",
                LocalDateTime.now().minusMinutes(1),
                null
        );

        List<Comment> comments1 = new ArrayList<>(List.of(comment, comment2));
        List<Comment> comments2 = new ArrayList<>(List.of(comment3));


        user.setPosts(List.of(post, post2));

        postRepository.saveAll(List.of(post, post2));

        userRepository.saveAll(List.of(user, user2, user3));

        post.setAuthor(user);
        post.setComments(List.of(comment, comment2, comment3));

        user.setComments(comments1);
        user2.setComments(comments2);

        commentRepository.saveAll(List.of(comment, comment2, comment3));

        comments1.forEach(comment1 -> comment1.setAuthor(user));
        comments2.forEach(comment1 -> comment1.setAuthor(user2));

        comments1.forEach(comment1 -> comment1.setPost(post));
        comments2.forEach(comment1 -> comment1.setPost(post));

        commentRepository.saveAll(List.of(comment, comment2, comment3));

    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    public void itShouldFindAllCommentsByAuthorId() {
        // given
        Long userId = user.getId();
        Long userId2 = user2.getId();
        int expectedSize = 2;
        int expectedSize2 = 1;

        // when
        List<Comment> comments = commentRepository.findAllCommentsByAuthorId(userId);
        List<Comment> comments2 = commentRepository.findAllCommentsByAuthorId(userId2);
        // then
        assertThat(comments.size()).isEqualTo(expectedSize);
        assertThat(comments2.size()).isEqualTo(expectedSize2);

    }

    @Test
    public void ItShouldReturnEmptyListWhenAuthorDoesNotHaveAnyComments() {
        // given
        Long userId = user3.getId();

        // when
        List<Comment> comments = commentRepository.findAllCommentsByAuthorId(userId);

        // then
        assertThat(comments.size()).isZero();
    }

    @Test
    public void ItShouldReturnEmptyListWhenAuthorDoesNotExists() {
        // given
        Long userId = 100L;

        // when
        List<Comment> comments = commentRepository.findAllCommentsByAuthorId(userId);

        // then
        assertThat(comments.size()).isZero();
    }

    @Test
    public void itShouldFindCommentByIdAndAuthorId() {
        // given
        Long commentId = comment.getId();
        Long userId = user.getId();
        // when
        Optional<Comment> expected = commentRepository.findCommentByIdAndAuthorId(commentId, userId);
        // then
        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get()).isEqualTo(comment);
    }

    @Test
    public void itShouldNotFindCommentByIdAndAuthorIdWhenAuthorDoesNotHaveCommentWithGivenId() {
        // given
        Long commentId = comment.getId();
        Long authorId = user2.getId();
        // when
        Optional<Comment> expected = commentRepository.findCommentByIdAndAuthorId(commentId, authorId);
        // then
        assertThat(expected.isPresent()).isFalse();
    }

    @Test
    public void itShouldNotFindCommentByIdAndAuthorIdWhenAuthorDoesNotExists() {
        //given
        Long commentId = comment.getId();
        Long authorId = 100L;

        //when
        Optional<Comment> expected = commentRepository.findCommentByIdAndAuthorId(commentId, authorId);

        //then
        assertThat(expected.isPresent()).isFalse();
    }

    @Test
    public void itShouldFindAllCommentsByPostId() {
        // given
        Long postId = post.getId();
        int expectedSize = 3;
        // when
        List<Comment> expected = commentRepository.findAllCommentsByPostId(postId);
        // then
        assertThat(expected.size()).isEqualTo(expectedSize);
    }

    @Test
    public void ItShouldReturnEmptyListWhenThereIsNoCommentsForPost() {
        //given
        Long postId = post2.getId();
        //when
        List<Comment> expected = commentRepository.findAllCommentsByPostId(postId);
        //then
        assertThat(expected.size()).isZero();
    }

    @Test
    public void ItShouldReturnEmptyListWhenPostDoesNotExists() {
        //given
        Long postId = 100L;
        //when
        List<Comment> expected = commentRepository.findAllCommentsByPostId(postId);
        //then
        assertThat(expected.size()).isZero();
    }

    @Test
    public void itShouldDeleteCommentByIdAndAuthorId() {
        //given
        Long commentId = comment.getId();
        Long authorId = user.getId();
        //when
        commentRepository.deleteCommentByIdAndAuthorId(commentId, authorId);
        //then
        assertThat(commentRepository.count()).isEqualTo(2);

    }

    @Test
    public void itShouldFindCommentByIdAndPostId() {
        //given

        Long commentId = comment.getId();
        Long postId = post.getId();

        //when

        Optional<Comment> expected = commentRepository.findCommentByIdAndPostId(commentId, postId);

        //then

        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get()).isEqualTo(comment);
    }

    @Test
    public void itShouldNotFindCommentByIdAndPostIdWhenCommentDoesNotExistsForPost() {
        //given
        Long commentId = 100L;
        Long postId = post.getId();

        //when
        Optional<Comment> expected = commentRepository.findCommentByIdAndPostId(commentId, postId);

        //then
        assertThat(expected.isPresent()).isFalse();
    }

    @Test
    public void itShouldNotFindCommentByIdAndPostIdWhenPostDoesNotExists() {
        //given
        Long commentId = comment.getId();
        Long postId = 100L;

        //when
        Optional<Comment> expected = commentRepository.findCommentByIdAndPostId(commentId, postId);

        //then
        assertThat(expected.isPresent()).isFalse();
    }
}