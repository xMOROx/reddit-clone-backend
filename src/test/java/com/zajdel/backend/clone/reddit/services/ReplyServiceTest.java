package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.Reply;
import com.zajdel.backend.clone.reddit.repositories.ReplyRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.ReplyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {
    @Mock
    private ReplyRepository replyRepository;
    ReplyService underTest;
    @BeforeEach
    void setUp() {
        underTest = new ReplyService(replyRepository);
    }

    @Test
    void canGetAllReplies() {
        // when
        underTest.getAllReplies();
        // then
        then(replyRepository).should().findAll();

    }

    @Test
    void canGetReplyById() {
        // given
        Reply reply = new Reply(
                null,
                null,
                null,
                "content of reply",
                LocalDateTime.now().minusMinutes(1),
                null
        );
        Long id = reply.getId();

        // when
        given(replyRepository.findById(id))
                .willReturn(Optional.of(reply));

        Reply expected = underTest.getReplyById(id);

        // then

        then(replyRepository).should().findById(id);

        assertThat(expected).isEqualTo(reply);
    }

    @Test
    void willThrowWhenGetReplyById() {
        // given
        Long id = 1L;

        // when
        given(replyRepository.findById(id))
                .willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> underTest.getReplyById(id))
                .isInstanceOf(ReplyNotFoundException.class)
                .hasMessageContaining("Reply not found with: id-"+id);

        then(replyRepository).should().findById(id);

    }

    @Test
    void canCreateNewReplyWithGivenLastModifiedDate() {
        // given
        Reply reply = new Reply(
                null,
                null,
                null,
                "content of reply",
                LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now().minusMinutes(1)
        );

        Long id = reply.getId();

        // when
        underTest.createNewReply(reply);

        ArgumentCaptor<Reply> replyArgumentCaptor = ArgumentCaptor.forClass(Reply.class);

        // then
        then(replyRepository).should().save(replyArgumentCaptor.capture());

        Reply capturedReply = replyArgumentCaptor.getValue();

        assertThat(capturedReply).isEqualTo(reply);
    }

    @Test
    void canCreateNewReplyWithoutGivenLastModifiedDate() {
        // given
        Reply reply = new Reply(
                null,
                null,
                null,
                "content of reply",
                LocalDateTime.now().minusMinutes(1),
                null
        );

        Long id = reply.getId();

        // when
        underTest.createNewReply(reply);

        ArgumentCaptor<Reply> replyArgumentCaptor = ArgumentCaptor.forClass(Reply.class);

        // then
        then(replyRepository).should().save(replyArgumentCaptor.capture());

        Reply capturedReply = replyArgumentCaptor.getValue();

        assertThat(capturedReply).isEqualTo(reply);
    }

    @Test
    void canRemoveReplyById() {
        // given
        Reply reply = new Reply(
                null,
                null,
                null,
                "content of reply",
                LocalDateTime.now().minusMinutes(1),
                null
        );
        Long id = reply.getId();

        // when
        given(replyRepository.findById(id))
                .willReturn(Optional.of(reply));

        underTest.removeReplyById(id);
        ArgumentCaptor<Long> replyArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // then
        then(replyRepository).should().deleteById(replyArgumentCaptor.capture());

        Long captureId = replyArgumentCaptor.getValue();

        assertThat(captureId).isEqualTo(id);
    }

    @Test
    void willThrownWhenReplyDoesNotExist() {
        // given
        Long id = 1L;

        // when
        given(replyRepository.findById(id))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.removeReplyById(id))
                .isInstanceOf(ReplyNotFoundException.class)
                .hasMessageContaining("Reply not found with: id-"+id);
        then(replyRepository).should().findById(id);
        then(replyRepository).should(never()).deleteById(any());
    }

    @Test
    void canFullUpdateReplyById() {
        // given
        Reply reply = new Reply(
                null,
                null,
                null,
                "content of reply",
                LocalDateTime.now().minusMinutes(1),
                null
        );
        Long id = reply.getId();

        Reply updatedReply = new Reply(
                null,
                null,
                null,
                "content of reply to update",
                LocalDateTime.now().plusMinutes(1),
                null
        );

        // when

        given(replyRepository.findById(id))
                .willReturn(Optional.of(reply));

        underTest.fullUpdateReplyById(id, updatedReply);

        ArgumentCaptor<Reply> replyArgumentCaptor = ArgumentCaptor.forClass(Reply.class);

        // then
        then(replyRepository).should().save(replyArgumentCaptor.capture());

        Reply capturedReply = replyArgumentCaptor.getValue();

        assertThat(capturedReply).isEqualTo(reply);
    }

    @Test
    void willCreateNewReplyWhenItDoesNotExistsByReplyId() {
        // given
        Reply reply = new Reply(
                null,
                null,
                null,
                "content of reply",
                LocalDateTime.now().minusMinutes(1),
                null
        );
        Long id = reply.getId();

        // when
        given(replyRepository.findById(id))
                .willReturn(Optional.empty());

        underTest.fullUpdateReplyById(id, reply);

        ArgumentCaptor<Reply> replyArgumentCaptor = ArgumentCaptor.forClass(Reply.class);

        // then
        then(replyRepository).should(times(2)).save(replyArgumentCaptor.capture());

        Reply capturedReply = replyArgumentCaptor.getValue();

        assertThat(capturedReply).isEqualTo(reply);

    }

    @Test
    void CanPartialUpdateReplyById() {
        // given
        Reply reply = new Reply(
                null,
                null,
                null,
                "content of reply",
                LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now().minusMinutes(1)
        );

        Long id = reply.getId();

        Reply updatedReply = new Reply(
                null,
                null,
                null,
                "content of reply to update",
                LocalDateTime.now().plusMinutes(1),
                LocalDateTime.now().plusMinutes(1)
        );

        // when

        given(replyRepository.findById(id))
                .willReturn(Optional.of(reply));

        underTest.partialUpdateReplyById(id, updatedReply);

        ArgumentCaptor<Reply> replyArgumentCaptor = ArgumentCaptor.forClass(Reply.class);

        // then
        then(replyRepository).should().save(replyArgumentCaptor.capture());

        Reply capturedReply = replyArgumentCaptor.getValue();

        assertThat(capturedReply).isEqualTo(reply);
    }

    @Test
    void willThrownWhenPartialUpdateReplyById() {
        // given
        Reply reply = new Reply(
                null,
                null,
                null,
                "content of reply",
                LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now().minusMinutes(1)
        );

        Long id = reply.getId();

        Reply updatedReply = new Reply(
                null,
                null,
                null,
                "content of reply to update",
                LocalDateTime.now().plusMinutes(1),
                LocalDateTime.now().plusMinutes(1)
        );

        // when

        given(replyRepository.findById(id))
                .willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> underTest.partialUpdateReplyById(id, updatedReply))
                .isInstanceOf(ReplyNotFoundException.class)
                .hasMessageContaining("Reply not found with: id-"+id);
        then(replyRepository).should().findById(id);
        then(replyRepository).should(never()).save(any());
    }
}