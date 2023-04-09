package com.zajdel.backend.clone.reddit.services;

import com.zajdel.backend.clone.reddit.entities.Reply;
import com.zajdel.backend.clone.reddit.repositories.ReplyRepository;
import com.zajdel.backend.clone.reddit.utils.exceptions.types.ReplyNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {
    ReplyRepository replyRepository;

    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public List<Reply> getAllReplies() {
        return replyRepository.findAll();
    }

    public Reply getReplyById(Long id) throws ReplyNotFoundException {
        return replyRepository.findById(id).orElseThrow(() -> new ReplyNotFoundException("id-" + id));
    }

    public Reply createNewReply(@NotNull Reply reply) {
        if (reply.getLastModifiedDate() == null) {
            reply.setLastModifiedDate(reply.getCreatedDate());
        }

        replyRepository.save(reply);
        return reply;
    }

    public void removeReplyById(Long id) throws ReplyNotFoundException {
        getReplyById(id);
        replyRepository.deleteById(id);
    }

    public Long fullUpdateReplyById(Long replyId, Reply reply) {
        Reply replyToUpdate;

        try {
            replyToUpdate = getReplyById(replyId);
        } catch (ReplyNotFoundException e) {
            createNewReply(reply);
            replyToUpdate = reply;
        }

        replyToUpdate.setContent(reply.getContent());
        replyToUpdate.setAuthor(reply.getAuthor());
        replyToUpdate.setVotes(reply.getVotes());
        replyToUpdate.setCreatedDate(reply.getCreatedDate());
        replyToUpdate.setLastModifiedDate(reply.getLastModifiedDate());

        replyRepository.save(replyToUpdate);

        return replyId;
    }

    public Long partialUpdateReplyById(Long postId, Reply reply) throws ReplyNotFoundException {
        Reply replyToUpdate = getReplyById(postId);

        if (reply.getContent() != null) {
            replyToUpdate.setContent(reply.getContent());
        }

        if (reply.getCreatedDate() != null) {
            replyToUpdate.setCreatedDate(reply.getCreatedDate());
        }

        if (reply.getLastModifiedDate() != null) {
            replyToUpdate.setLastModifiedDate(reply.getLastModifiedDate());
        }

        replyRepository.save(replyToUpdate);

        return postId;
    }


}
