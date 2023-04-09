package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.Post;
import com.zajdel.backend.clone.reddit.entities.User;
import com.zajdel.backend.clone.reddit.repositories.PostRepository;
import com.zajdel.backend.clone.reddit.repositories.UserRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.PostNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.PostNotFoundForUserException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    PostService underTest;
    @BeforeEach
    void setUp() {
        underTest = new PostService(postRepository, userRepository);
    }

    @Test
    void canGtAllPosts() {
        // when
        underTest.getAllPosts();
        // then
        then(postRepository).should().findAll();
    }

    @Test
    void canGetPostById() {
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
        Long id = post.getId();

        // when
        given(postRepository.findById(id))
                .willReturn(Optional.of(post));

        Post expected = underTest.getPostById(id);

        // then

        assertThat(expected).isEqualTo(post);

        then(postRepository).should().findById(id);

    }

    @Test
    void willThrowWhenPostNotFoundById() {
        // given
        Long id = 1L;
        // when
        given(postRepository.findById(id))
                .willReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> underTest.getPostById(id))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("Post not found with: id-" + id);
        then(postRepository).should().findById(id);
    }

    @Test
    void canCreateNewPostWithGivenLastModifiedDate() {
        // given
        Post post = new Post(
                null,
                "title",
                "description of post",
                null,
                null,
                null,
                List.of("url1", "url2"),
                LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now().minusMinutes(1)
        );

        // when
        underTest.createNewPost(post);
        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        // then
        then(postRepository).should().save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        assertThat(capturedPost).isEqualTo(post);

    }

    @Test
    void canCreateNewPostWithoutGivenLastModifiedDate() {
        // given
        Post post = new Post(
                null,
                "title",
                "description of post",
                null,
                null,
                null,
                List.of("url1", "url2"),
                LocalDateTime.now().minusMinutes(1),
                null
        );

        // when
        underTest.createNewPost(post);
        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        // then
        then(postRepository).should().save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        assertThat(capturedPost).isEqualTo(post);

    }

    @Test
    void canRemovePostById() {
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
        Long id = post.getId();
        // when
        given(postRepository.findById(id))
                .willReturn(Optional.of(post));

        underTest.removePostById(id);

        // then
        then(postRepository).should().findById(id);
        then(postRepository).should().deleteById(id);
    }

    @Test
    void willThrowWhenPostNotFoundByIdForRemove() {
        // given
        Long id = 1L;
        // when
        given(postRepository.findById(id))
                .willReturn(Optional.empty());
        // then
        assertThatThrownBy(() -> underTest.removePostById(id))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("Post not found with: id-" + id);

        then(postRepository).should().findById(id);
        then(postRepository).should(never()).deleteById(id);
    }


    @Test
    void getPostByIdForUserById() {
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
        Long id = post.getId();
        Long userId = user.getId();

        // when
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(postRepository.findPostByIdAndAuthorId(id, userId))
                .willReturn(Optional.of(post));

        Post expected = underTest.getPostByIdForUserById(id, userId);

        // then
        then(userRepository).should().findById(userId);
        then(postRepository).should().findPostByIdAndAuthorId(id, userId);
        assertThat(expected).isEqualTo(post);
    }

    @Test
    void willThrownWhenUserNotFoundForPostByIdAndUserId() {
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

        Long id = post.getId();
        Long userId = 1L;

        // when

        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> underTest.getPostByIdForUserById(id, userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with: id-" + userId);

        then(userRepository).should().findById(userId);
        then(postRepository).should(never()).findPostByIdAndAuthorId(id, userId);
    }

    @Test
    void willThrownWhenPostNotFoundForUserByIdAndUserId() {
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
        Long id = 1L;
        Long userId = user.getId();

        // when
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        given(postRepository.findPostByIdAndAuthorId(id, userId))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.getPostByIdForUserById(id, userId))
                .isInstanceOf(PostNotFoundForUserException.class)
                .hasMessageContaining("Post not found with: " +"Post id-" + id + "for user id-" + userId );

        then(userRepository).should().findById(userId);
        then(postRepository).should().findPostByIdAndAuthorId(id, userId);
    }

    @Test
    void getPostsForUserById() {
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
        Long id = user.getId();

        // when
        given(userRepository.findById(id))
                .willReturn(Optional.of(user));
        underTest.getPostsByUserId(id);
        // then
        then(userRepository).should().findById(id);
        then(postRepository).should().findAllPostsByAuthorId(id);
    }

    @Test
    void willThrownWhenUserNotFoundForGetPostsByUserId() {
        // given
        Long id = 1L;

        // when
        given(userRepository.findById(id))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.getPostsByUserId(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with: id-" + id);

        then(userRepository).should().findById(id);
        then(postRepository).should(never()).findAllPostsByAuthorId(id);
    }
    @Test
    void canRemovePostByUserId() {
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
        Long id = post.getId();
        Long userId = user.getId();

        // when
        given(postRepository.findPostByIdAndAuthorId(id, userId))
                .willReturn(Optional.of(post));
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));

        ArgumentCaptor<Long> postIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> authorIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        underTest.removePostByUserId(id, userId);

        // then

        then(postRepository).should().findPostByIdAndAuthorId(id, userId);
        then(postRepository).should().deletePostByIdAndAuthorId(postIdArgumentCaptor.capture(), authorIdArgumentCaptor.capture());

        Long capturedPostId = postIdArgumentCaptor.getValue();
        Long capturedUserId = authorIdArgumentCaptor.getValue();

        assertThat(capturedPostId).isEqualTo(id);
        assertThat(capturedUserId).isEqualTo(userId);


    }

    @Test
    void willThrownWhenUserNotFoundForRemovePostByUserId() {
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
        Long id = post.getId();
        Long userId = 1L;

        // when
        given(userRepository.findById(userId))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.removePostByUserId(id, userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with: id-" + userId);
        then(userRepository).should().findById(userId);
        then(postRepository).should(never()).findPostByIdAndAuthorId(id, userId);
        then(postRepository).should(never()).deletePostByIdAndAuthorId(id, userId);
    }

    @Test
    void willThrownWhenPostNotFoundForRemovePostByUserId() {
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
        Long id = 1L;
        Long userId = user.getId();
        // when

        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(postRepository.findPostByIdAndAuthorId(id, userId))
                .willReturn(Optional.empty());


        // then

        assertThatThrownBy(() -> underTest.removePostByUserId(id, userId))
                .isInstanceOf(PostNotFoundForUserException.class)
                .hasMessageContaining("Post not found with: " +"Post id-" + id + "for user id-" + userId );

        then(userRepository).should().findById(userId);
        then(postRepository).should().findPostByIdAndAuthorId(id, userId);
        then(postRepository).should(never()).deletePostByIdAndAuthorId(id, userId);

    }

    @Test
    void canFullUpdatePostById() {
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

        Long id = post.getId();

        Post updatedPost = new Post(
                null,
                "Updated title",
                "Updated description of post",
                null,
                null,
                null,
                List.of("Updated url1", "Updated url2"),
                null,
                null
        );

        // when
        given(postRepository.findById(id))
                .willReturn(Optional.of(post));

        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        Long updatedId =  underTest.fullUpdatePostById(id, updatedPost);

        // then
        then(postRepository).should().findById(id);
        then(postRepository).should().save(postArgumentCaptor.capture());


        Post capturedPost = postArgumentCaptor.getValue();

        assertThat(capturedPost).isEqualTo(post);
        assertThat(updatedId).isEqualTo(id);
    }

    @Test
    void willCreateNewPostWhenItDoesNotExistsByPostId() {
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
        Long id = post.getId();

        // when
        given(postRepository.findById(id))
                .willReturn(Optional.empty());

        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        Long updatedId =  underTest.fullUpdatePostById(id, post);

        // then
        then(postRepository).should().findById(id);
        then(postRepository).should(times(2)).save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        assertThat(capturedPost).isEqualTo(post);

    }

    @Test
    void canPartialUpdatePostById() {
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

        Post updatedPost = new Post(
                null,
                "Updated title",
                "Updated description of post",
                null,
                null,
                null,
                List.of("Updated url1", "Updated url2"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Long id = post.getId();

        // when

        given(postRepository.findById(id))
                .willReturn(Optional.of(post));

        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        Long updatedId =  underTest.partialUpdatePostById(id, updatedPost);

        // then
        then(postRepository).should().findById(id);
        then(postRepository).should().save(postArgumentCaptor.capture());

        Post capturedPost = postArgumentCaptor.getValue();

        assertThat(capturedPost).isEqualTo(post);
        assertThat(updatedId).isEqualTo(id);
    }

    @Test
    void willThrownWhenPostNotFoundForPartialUpdatePostById() {
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

        Post updatedPost = new Post(
                null,
                "Updated title",
                "Updated description of post",
                null,
                null,
                null,
                List.of("Updated url1", "Updated url2"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Long id = post.getId();

        // when

        given(postRepository.findById(id))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.partialUpdatePostById(id, updatedPost))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("Post not found with: id-" + id);

        then(postRepository).should().findById(id);
        then(postRepository).should(never()).save(updatedPost);
    }


}