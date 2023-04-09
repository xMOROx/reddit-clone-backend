package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.SubReddit;
import com.zajdel.backend.clone.reddit.entities.User;
import com.zajdel.backend.clone.reddit.repositories.SubRedditRepository;
import com.zajdel.backend.clone.reddit.repositories.UserRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.SubRedditAlreadyExistsException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.SubRedditNotFoundException;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SubRedditServiceTest {
    @Mock
    private SubRedditRepository subRedditRepository;
    @Mock
    private UserRepository userRepository;
    private SubRedditService underTest;
    @BeforeEach
    void setUp() {
        underTest = new SubRedditService(subRedditRepository, userRepository);
    }

    @Test
    void canGetAllSubReddits() {
        // when
        underTest.getAllSubReddits();

        //then
        then(subRedditRepository).should().findAll();
    }

    @Test
    void canGetSubRedditById() {
        // given
        SubReddit subReddit = new SubReddit(
                "SubRedditName",
                "SubRedditDescription",
                null,
                null,
                "SubRedditBannerUrl",
                null
        );
        Long id = subReddit.getId();

        // when
        given(subRedditRepository.findById(id)).willReturn(Optional.of(subReddit));

        SubReddit expectedSubReddit = underTest.getSubRedditById(id);

        // then
        then(subRedditRepository).should().findById(id);
        assertThat(expectedSubReddit).isEqualTo(subReddit);

    }

    @Test
    void willThrownWhenSubRedditIsNotFound() {
        // given
        Long id = 1L;

        // when
        given(subRedditRepository.findById(id)).willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> underTest.getSubRedditById(id))
                .isInstanceOf(SubRedditNotFoundException.class)
                .hasMessageContaining("SubReddit not found with: id-" + id);

        then(subRedditRepository).should().findById(id);
        then(subRedditRepository).should(never()).findAll();
    }

    @Test
    void canCreateNewSubReddit() {
        // given
        SubReddit subReddit = new SubReddit(
                "SubRedditName",
                "SubRedditDescription",
                null,
                null,
                "SubRedditBannerUrl",
                null
        );

        // when

        given(subRedditRepository.findSubRedditByName(subReddit.getName())).willReturn(Optional.empty());

        ArgumentCaptor<SubReddit> subRedditArgumentCaptor = ArgumentCaptor.forClass(SubReddit.class);

        underTest.createNewSubReddit(subReddit);

        // then

        then(subRedditRepository).should().save(subRedditArgumentCaptor.capture());

        SubReddit capturedSubReddit = subRedditArgumentCaptor.getValue();

        assertThat(capturedSubReddit).isEqualTo(subReddit);

    }

    @Test
    void willThrownWhenSubRedditAlreadyExists() {
        // given
        SubReddit subReddit = new SubReddit(
                "SubRedditName",
                "SubRedditDescription",
                null,
                null,
                "SubRedditBannerUrl",
                null
        );

        // when
        given(subRedditRepository.findSubRedditByName(subReddit.getName())).willReturn(Optional.of(subReddit));

        // then
        assertThatThrownBy(() -> underTest.createNewSubReddit(subReddit))
                .isInstanceOf(SubRedditAlreadyExistsException.class)
                .hasMessageContaining("Subreddit already exists with name: " + subReddit.getName());
        then(subRedditRepository).should().findSubRedditByName(subReddit.getName());
        then(subRedditRepository).should(never()).save(any());
    }

    @Test
    void canGetAllUserSubReddits() {
        // given
        Long userId = 1L;
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
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        underTest.getAllUserSubReddits(userId);

        // then

        then(userRepository).should().findById(userId);

    }

    @Test
    void willThrownWhenUserDoesNotExist() {
        // given
        Long userId = 1L;

        // when
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.getAllUserSubReddits(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with: id-" + userId);
        then(userRepository).should().findById(userId);
        then(subRedditRepository).should(never()).findAllSubredditsByOwnerId(any());
    }

    @Test
    void canRemoveSubRedditById() {
        // given
        SubReddit subReddit = new SubReddit(
                "SubRedditName",
                "SubRedditDescription",
                null,
                null,
                "SubRedditBannerUrl",
                null
        );
        // when
        given(subRedditRepository.findById(subReddit.getId()))
                .willReturn(Optional.of(subReddit));

        underTest.removeSubRedditById(subReddit.getId());
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // then
        then(subRedditRepository).should().deleteById(longArgumentCaptor.capture());

        Long capturedId = longArgumentCaptor.getValue();

        assertThat(capturedId).isEqualTo(subReddit.getId());
    }

    @Test
    void willThrownWhenSubRedditDoesNotExist() {
        // given
        Long id = 1L;

        // when
        given(subRedditRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.removeSubRedditById(id))
                .isInstanceOf(SubRedditNotFoundException.class)
                .hasMessageContaining("SubReddit not found with: id-" + id);

        then(subRedditRepository).should(never()).deleteById(any());
    }

    @Test
    void canFullUpdateSubRedditById() {
        // given
        SubReddit subReddit = new SubReddit(
                "SubRedditName",
                "SubRedditDescription",
                null,
                null,
                "SubRedditBannerUrl",
                null
        );

        SubReddit updatedSubReddit = new SubReddit(
                "UpdatedSubRedditName",
                "UpdatedSubRedditDescription",
                null,
                null,
                "UpdatedSubRedditBannerUrl",
                null
        );
        // when
        given(subRedditRepository.findById(subReddit.getId()))
                .willReturn(Optional.of(subReddit));

        underTest.fullUpdateSubRedditById(subReddit.getId(), updatedSubReddit);

        ArgumentCaptor<SubReddit> subRedditArgumentCaptor = ArgumentCaptor.forClass(SubReddit.class);

        // then
        then(subRedditRepository).should().save(subRedditArgumentCaptor.capture());

        SubReddit capturedSubReddit = subRedditArgumentCaptor.getValue();

        assertThat(capturedSubReddit).isEqualTo(subReddit);

    }

    @Test
    void willCreateNewSubRedditWhenDoesNotExists() {
        // given
        SubReddit subReddit = new SubReddit(
                "SubRedditName",
                "SubRedditDescription",
                null,
                null,
                "SubRedditBannerUrl",
                null
        );



        // when
        given(subRedditRepository.findById(subReddit.getId()))
                .willReturn(Optional.empty());

        underTest.fullUpdateSubRedditById(subReddit.getId(), subReddit);

        ArgumentCaptor<SubReddit> subRedditArgumentCaptor = ArgumentCaptor.forClass(SubReddit.class);

        // then
        then(subRedditRepository).should(times(2)).save(subRedditArgumentCaptor.capture());

        SubReddit capturedSubReddit = subRedditArgumentCaptor.getValue();

        assertThat(capturedSubReddit).isEqualTo(subReddit);
    }

    @Test
    void canPartialUpdateSubReddit() {
        // given
        SubReddit subReddit = new SubReddit(
                "SubRedditName",
                "SubRedditDescription",
                null,
                null,
                "SubRedditBannerUrl",
                null
        );

        // when

        given(subRedditRepository.findById(subReddit.getId()))
                .willReturn(Optional.of(subReddit));

        subReddit.setName("NewName");
        subReddit.setDescription("NewDescription");
        subReddit.setBannerUrl("NewBannerUrl");

        underTest.partialUpdateSubRedditById(subReddit.getId(), subReddit);

        ArgumentCaptor<SubReddit> subRedditArgumentCaptor = ArgumentCaptor.forClass(SubReddit.class);

        // then

        then(subRedditRepository).should().save(subRedditArgumentCaptor.capture());

        SubReddit capturedSubReddit = subRedditArgumentCaptor.getValue();

        assertThat(capturedSubReddit).isEqualTo(subReddit);
    }

    @Test
    void willThrownWhenSubRedditDoesNotExistForPartialUpdate() {
        // given
        SubReddit subReddit = new SubReddit(
                "SubRedditName",
                "SubRedditDescription",
                null,
                null,
                "SubRedditBannerUrl",
                null
        );

        // when

        given(subRedditRepository.findById(subReddit.getId()))
                .willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> underTest.partialUpdateSubRedditById(subReddit.getId(), subReddit))
                .isInstanceOf(SubRedditNotFoundException.class)
                .hasMessageContaining("SubReddit not found with: id-" + subReddit.getId());

        then(subRedditRepository).should(never()).save(any());
    }
}