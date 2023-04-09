package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.*;
import com.zajdel.backend.clone.reddit.repositories.UserRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserAlreadyExistsException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    //    private AutoCloseable autoCloseable;
    private UserService underTest;

    @BeforeEach
    void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new UserService(userRepository);

    }


    @Test
    void canGetAllUsers() {
        // when
        underTest.getAllUsers();

        // then
        verify(userRepository).findAll();
    }

    @Test
    void canGetUserById() {
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

        // when

        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));
        User expected = underTest.getUserById(user.getId());
        // then

        assertThat(expected).isEqualTo(user);
    }

    @Test
    void willThrownWhenUserDoesNotExist() {
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
        // when

        given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.getUserById(user.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with: id-" + user.getId());

        then(userRepository).should().findById(any());

    }

    @Test
    void canCreateNewUser() {
        //given
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

        // when
        underTest.createNewUser(user);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // then
        then(userRepository).should().save((userArgumentCaptor.capture()));

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void willThrownWhenUsernameIsTaken() {
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


        // when
        given(userRepository.findUserByUsername(user.getUsername()))
                .willReturn(Optional.of(user));

//        when(userRepository.findUserByUsername(user.getUsername()))
//                .thenReturn(Optional.of(user));

        //then
        assertThatThrownBy(() -> underTest.createNewUser(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User with username: " + user.getUsername() + " already exists");

//        verify(userRepository, never()).save(any());
        then(userRepository).should(never()).save(any());
    }

    @Test
    void willThrownWhenEmailIsTaken() {
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
        // when

        given(userRepository.findUserByEmail(user.getEmail()))
                .willReturn(Optional.of(user));

        // then

        assertThatThrownBy(() -> underTest.createNewUser(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User with email: " + user.getEmail() + " already exists");

        then(userRepository).should(never()).save(any());
    }

    @Test
    void canRemoveUserById() {
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

        // when
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));

        underTest.removeUserById(user.getId());
        ArgumentCaptor<Long> userArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // then
        then(userRepository).should().deleteById(userArgumentCaptor.capture());

        Long capturedId = userArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(user.getId());
    }

    @Test
    void willThrownWhenUserDoesNotExists() {
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
        // when

        given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.removeUserById(user.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with: id-" + user.getId());

        then(userRepository).should(never()).deleteById(any());

    }

    @Test
    void canFullUpdateExistingUser() {
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

        User updatedUser = new User(
                "updatedUsername1",
                "updatedPassword1",
                "updatedEmail1@gmail.com",
                null,
                null,
                null,
                null,
                null,
                null);
        // when



        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));

        underTest.fullUpdateUserById(user.getId(), updatedUser);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // then
        then(userRepository).should().save(userArgumentCaptor.capture());

        User value = userArgumentCaptor.getValue();

        assertThat(value).isEqualTo(user);

    }

    @Test
    void willCreateNewUserWhenDoesNotExists() {
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

        // when
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());

        underTest.fullUpdateUserById(user.getId(), user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // then
        then(userRepository).should(times(2)).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();

        assertThat(capturedUser).isEqualTo(user);

    }

    @Test
    void canPartialUpdate() {
        // give
        User user = new User(
                "username1",
                "password1",
                "email1@gmail.com",
                List.of(new Post()),
                List.of(new Vote()),
                List.of(new Comment()),
                List.of(new Reply()),
                List.of(new SubReddit()),
                List.of(new SubReddit())
        );

        // when
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));

        user.setUsername("newUsername");
        user.setPassword("newPassword");
        user.setEmail("newEmail@gmai.com");

        underTest.partialUpdateUserById(user.getId(), user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // then
        then(userRepository).should().save(userArgumentCaptor.capture());

        User value = userArgumentCaptor.getValue();

        assertThat(value).isEqualTo(user);
    }

    @Test
    void willThrownWhenUserDoesNotExistsForPartialUpdate() {
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

        // when
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> underTest.partialUpdateUserById(user.getId(), user))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with: id-" + user.getId());

        then(userRepository).should(never()).save(any());
    }
}