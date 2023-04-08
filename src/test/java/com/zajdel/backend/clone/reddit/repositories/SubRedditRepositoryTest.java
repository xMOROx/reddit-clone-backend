package com.zajdel.backend.clone.reddit.repositories;

import com.zajdel.backend.clone.reddit.entities.SubReddit;
import com.zajdel.backend.clone.reddit.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class SubRedditRepositoryTest {
    @Autowired
    private SubRedditRepository underTest;
    @Autowired
    private UserRepository userRepository;

    User user;
    User user2;
    User user3;

    @BeforeEach
    void setUp() {
        SubReddit subReddit1 = new SubReddit(
                "Subreddit1",
                "Description1",
                null,
                null,
                "ULR1",
                null
        );

        SubReddit subReddit2 = new SubReddit(
                "Subreddit2",
                "Description2",
                null,
                null,
                "ULR2",
                null
        );

        SubReddit subReddit3 = new SubReddit(
                "Subreddit3",
                "Description3",
                null,
                null,
                "ULR3",
                null
        );


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

        List<SubReddit> subReddits = new ArrayList<>();
        List<SubReddit> ownedSubReddits = new ArrayList<>();
        List<SubReddit> ownedSubReddits2 = new ArrayList<>();

        subReddits.add(subReddit1);
        subReddits.add(subReddit2);
        subReddits.add(subReddit3);

        ownedSubReddits.add(subReddit1);
        ownedSubReddits.add(subReddit2);

        ownedSubReddits2.add(subReddit3);

        user.setSubreddits(subReddits);
        user.setOwnedSubreddits(ownedSubReddits);

        user2.setOwnedSubreddits(ownedSubReddits2);
        user2.setSubreddits(subReddits);

        underTest.saveAll(subReddits);

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);


        ownedSubReddits.forEach(subReddit -> subReddit.setOwner(user));
        ownedSubReddits2.forEach(subReddit -> subReddit.setOwner(user2));

        underTest.saveAll(ownedSubReddits);
        underTest.saveAll(ownedSubReddits2);

    }

    @AfterEach
    public void tearDown() {
        underTest.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void ifShouldFindAllSubRedditsByOwnerId() {
        // given
        Long ownerId = user.getId();
        Long ownerId2 = user2.getId();
        Long ownerId3 = user3.getId();
        int expectedSize1 = 2;
        int expectedSize2 = 1;
        // when

        List<SubReddit> expected = underTest.findAllByOwnerId(ownerId);
        List<SubReddit> expected2 = underTest.findAllByOwnerId(ownerId2);
        List<SubReddit> expected3 = underTest.findAllByOwnerId(ownerId3);
        // then
        assertThat(expected.size()).isEqualTo(expectedSize1);
        assertThat(expected2.size()).isEqualTo(expectedSize2);
        assertThat(expected3.size()).isZero();

    }

}