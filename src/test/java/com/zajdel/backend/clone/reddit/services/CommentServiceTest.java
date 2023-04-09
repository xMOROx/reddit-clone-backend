package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.Comment;
import com.zajdel.backend.clone.reddit.entities.Post;
import com.zajdel.backend.clone.reddit.entities.User;
import com.zajdel.backend.clone.reddit.repositories.CommentRepository;
import com.zajdel.backend.clone.reddit.repositories.PostRepository;
import com.zajdel.backend.clone.reddit.repositories.UserRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;

    private CommentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CommentService(commentRepository, userRepository, postRepository);
    }

    @Test
    void canGetAllComments() {
        // when
        underTest.getAllComments();
        // then
        then(commentRepository).should().findAll();
    }

    @Test
    void canGetCommentById() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            null
        );

        Long id = comment.getId();

        // when
        given(commentRepository.findById(id)).willReturn(Optional.of(comment));
        underTest.getCommentById(id);

        // then
        then(commentRepository).should().findById(id);

    }

    @Test
    void willThrownWhenCommentNotFound() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            null
        );

        Long id = comment.getId();

        // when
        given(commentRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.getCommentById(id))
            .isInstanceOf(CommentNotFoundException.class)
            .hasMessageContaining("Comment not found with: id-" + id);

        then(commentRepository).should().findById(id);
    }

    @Test
    void canCreateNewCommentWithGivenLastModifiedDate() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        // when

        underTest.createNewComment(comment);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        // then
        then(commentRepository).should().save(commentArgumentCaptor.capture());

        Comment capturedComment = commentArgumentCaptor.getValue();

        assertThat(capturedComment).isEqualTo(comment);

    }

    @Test
    void canCreateNewCommentWithoutGivenLastModifiedDate() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            null
        );

        // when

        underTest.createNewComment(comment);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        // then
        then(commentRepository).should().save(commentArgumentCaptor.capture());

        Comment capturedComment = commentArgumentCaptor.getValue();

        assertThat(capturedComment).isEqualTo(comment);

    }

    @Test
    void canRemoveCommentById() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            null
        );

        Long id = comment.getId();

        // when
        given(commentRepository.findById(id))
                .willReturn(Optional.of(comment));

        underTest.removeCommentById(id);

        ArgumentCaptor<Long> commentIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // then
        then(commentRepository).should().findById(id);
        then(commentRepository).should().deleteById(commentIdArgumentCaptor.capture());

        Long capturedCommentId = commentIdArgumentCaptor.getValue();

        assertThat(capturedCommentId).isEqualTo(id);


    }

    @Test
    void canFullUpdateCommentById() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Long id = comment.getId();

        Comment updatedComment = new Comment(
            null,
            null,
            null,
            null,
            "Updated content of comment",
            LocalDateTime.now().plusMinutes(4),
            LocalDateTime.now().plusMinutes(4)
        );

        // when
        given(commentRepository.findById(id))
                .willReturn(Optional.of(comment));

        underTest.fullUpdateCommentById(id, updatedComment);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        // then
        then(commentRepository).should().findById(id);
        then(commentRepository).should().save(commentArgumentCaptor.capture());

        Comment capturedComment = commentArgumentCaptor.getValue();

        assertThat(capturedComment).isEqualTo(comment);

    }

    @Test
    void willCreateNewCommentWhenItDoesNotExistsByCommentId() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Long id = comment.getId();

        // when
        given(commentRepository.findById(id))
                .willReturn(Optional.empty());

        underTest.fullUpdateCommentById(id, comment);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        // then
        then(commentRepository).should().findById(id);
        then(commentRepository).should(times(2)).save(commentArgumentCaptor.capture());

        Comment capturedComment = commentArgumentCaptor.getValue();

        assertThat(capturedComment).isEqualTo(comment);
    }

    @Test
    void canPartialUpdateCommentById() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Long id = comment.getId();

        Comment updatedComment = new Comment(
            null,
            null,
            null,
            null,
            "Updated content of comment",
            LocalDateTime.now().plusMinutes(4),
            LocalDateTime.now().plusMinutes(4)
        );

        // when

        given(commentRepository.findById(id))
                .willReturn(Optional.of(comment));

        underTest.partialUpdateCommentById(id, updatedComment);

        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);

        // then

        then(commentRepository).should().findById(id);
        then(commentRepository).should().save(commentArgumentCaptor.capture());

        Comment capturedComment = commentArgumentCaptor.getValue();

        assertThat(capturedComment).isEqualTo(comment);

    }

    @Test
    void getCommentsByPostId() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Post post = new Post(
                null,
                "title",
                "description of post",
                null,
                null,
                null,
                List.of("url1", "url2"),
                null,
                null
        );

        Long postId = post.getId();

        // when
        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        underTest.getCommentsByPostId(postId);

        // then
        then(postRepository).should().findById(postId);
    }

    @Test
    void willThrownWhenPostDoesNotExist() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Long postId = 1L;

        // when
        given(postRepository.findById(postId))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.getCommentsByPostId(postId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("Post not found with: id-" + postId);

        then(postRepository).should().findById(postId);
        then(commentRepository).should(never()).findAllCommentsByPostId(postId);
    }

    @Test
    void canGetCommentsByUserId() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        User user = new User(
                "username1",
                "password1",
                "email1@gmail.com",
                null,
                null,
                null,
                null,
                null,
                null);

        Long userId = user.getId();

        // when

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        underTest.getCommentsByUserId(userId);

        // then
        then(userRepository).should().findById(userId);

    }

    @Test
    void willThrownWhenUserDoesNotExist() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        long userId = 1L;

        // when
        given(userRepository.findById(1L))
                .willReturn(Optional.empty());


        // then
        assertThatThrownBy(() -> underTest.getCommentsByUserId(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with: id-" + userId);

        then(userRepository).should().findById(userId);
        then(commentRepository).should(never()).findAllCommentsByAuthorId(userId);

    }

    @Test
    void getCommentByIdAndUserId() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        User user = new User(
                "username1",
                "password1",
                "email1@gmail.com",
                null,
                null,
                null,
                null,
                null,
                null);

        Long userId = user.getId();
        Long commentId = comment.getId();

        // when
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(commentRepository.findCommentByIdAndAuthorId(commentId, userId))
                .willReturn(Optional.of(comment));


        Comment expected = underTest.getCommentByIdAndUserId(commentId, userId);

        // then
        then(userRepository).should().findById(userId);
        then(commentRepository).should().findCommentByIdAndAuthorId(commentId, userId);

        assertThat(expected).isEqualTo(comment);

    }

    @Test
    void willThrownWhenCommentNotFoundForUserById() {
        // given
        User user = new User(
                "username1",
                "password1",
                "email1@gmail.com",
                null,
                null,
                null,
                null,
                null,
                null);

        Long userId = user.getId();
        Long commentId = 1L;

        // when
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(commentRepository.findCommentByIdAndAuthorId(commentId, userId))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.getCommentByIdAndUserId(commentId, userId))
                .isInstanceOf(CommentNotFoundForUserException.class)
                .hasMessageContaining("Comment not found for user with: id-" + commentId + " and userId-" + userId);

        then(userRepository).should().findById(userId);
        then(commentRepository).should().findCommentByIdAndAuthorId(commentId, userId);

    }

    @Test
    void willThrownWhenUserNotFound() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Long userId = 1L;

        // when

        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> underTest.getCommentByIdAndUserId(comment.getId(), userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with: id-" + userId);

        then(userRepository).should().findById(userId);
        then(commentRepository).should(never()).findCommentByIdAndAuthorId(anyLong(), anyLong());
    }

    @Test
    void canGetCommentByIdAndPostId() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Post post = new Post(
                null,
                "title",
                "description of post",
                null,
                null,
                null,
                List.of("url1", "url2"),
                null,
                null
        );

        Long postId = post.getId();
        Long commentId = comment.getId();

        // when
        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        given(commentRepository.findCommentByIdAndPostId(commentId, postId))
                .willReturn(Optional.of(comment));

        Comment expected = underTest.getCommentByIdAndPostId(commentId, postId);

        // then
        then(postRepository).should().findById(postId);
        then(commentRepository).should().findCommentByIdAndPostId(commentId, postId);

        assertThat(expected).isEqualTo(comment);
    }

    @Test
    void willThrownWhenCommentNotFoundForPostById() {
        // given
        Post post = new Post(
                null,
                "title",
                "description of post",
                null,
                null,
                null,
                List.of("url1", "url2"),
                null,
                null
        );

        Long postId = post.getId();

        Long commentId = 1L;

        // when

        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        given(commentRepository.findCommentByIdAndPostId(commentId, postId))
                .willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> underTest.getCommentByIdAndPostId(commentId, postId))
                .isInstanceOf(CommentNotFoundForPostException.class)
                .hasMessageContaining("Comment not found for post with: id-" + commentId + " and postId-" + postId);


        then(postRepository).should().findById(postId);
        then(commentRepository).should().findCommentByIdAndPostId(commentId, postId);
    }

    @Test
    void willThrownWhenPostNotFound() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Long postId = 1L;
        Long commentId = comment.getId();

        // when
        given(postRepository.findById(postId))
                .willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> underTest.getCommentByIdAndPostId(commentId, postId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("Post not found with: id-" + postId);

        then(postRepository).should().findById(postId);
        then(commentRepository).should(never()).findCommentByIdAndPostId(anyLong(), anyLong());
    }
    @Test
    void removeCommentByIdAndUserId() {
        // given
        Comment comment = new Comment(
            null,
            null,
            null,
            null,
            "Content of comment",
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        User user = new User(
                "username1",
                "password1",
                "email1@gmail.com",
                null,
                null,
                null,
                null,
                null,
                null);

        Long userId = user.getId();
        Long commentId = comment.getId();

        // when
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(commentRepository.findCommentByIdAndAuthorId(commentId, userId))
                .willReturn(Optional.of(comment));

        underTest.removeCommentByIdAndUserId(commentId, userId);

        // then
        then(userRepository).should().findById(userId);
        then(commentRepository).should().findCommentByIdAndAuthorId(commentId, userId);
        then(commentRepository).should().deleteCommentByIdAndAuthorId(commentId, userId);



    }
}