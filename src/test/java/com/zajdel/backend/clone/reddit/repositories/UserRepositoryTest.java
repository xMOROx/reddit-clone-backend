package com.zajdel.backend.clone.reddit.repositories;

import com.zajdel.backend.clone.reddit.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;
    User user;
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

        underTest.save(user);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    public void ItShouldFindUserByUsername() {
        //given
        String username = "username1";

        //when
        Optional<User> expected = underTest.findUserByUsername(username);

        //then
        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get()).isEqualTo(user);
    }

    @Test
    public void ItShouldNotFindUserByUsernameWhenUserDoesNotExists() {
        //given
        String username = "username10";

        //when
        Optional<User> expected = underTest.findUserByUsername(username);

        //then
        assertThat(expected.isPresent()).isFalse();
    }

    @Test
    public void ItShouldFindUserByEmail() {
        //given
        String email = "email1@gmail.com";

        //when
        Optional<User> expected = underTest.findUserByEmail(email);

        //then
        assertThat(expected.isPresent()).isTrue();
        assertThat(expected.get()).isEqualTo(user);
    }

    @Test
    public void ItShouldNotFindUserByEmailWhenUserDoesNotExists() {
        //given
        String email2 = "email123@gmail.com";

        //when
        Optional<User> expected = underTest.findUserByEmail(email2);

        //then
        assertThat(expected.isPresent()).isFalse();
    }

}