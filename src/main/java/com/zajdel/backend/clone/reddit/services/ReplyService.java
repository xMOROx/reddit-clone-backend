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

    /**
     * Get all replies
     * @return List of all replies
     */
    public List<Reply> getAllReplies() {
        return replyRepository.findAll();
    }
    /**
     * Get reply by id
     * @param id id of reply
     * @return Reply
     * @throws ReplyNotFoundException If reply not found
     */
    public Reply getReplyById(Long id) throws ReplyNotFoundException {
        return replyRepository.findById(id).orElseThrow(() -> new ReplyNotFoundException("id-" + id));
    }

    /**
     * Create new reply. If reply has no last modified date, it will be set to created date.
     * @param reply Reply to create
     * @return Created reply
     */
    public Reply createNewReply(@NotNull Reply reply) {
        if (reply.getLastModifiedDate() == null) {
            reply.setLastModifiedDate(reply.getCreatedDate());
        }

        replyRepository.save(reply);
        return reply;
    }
    /**
     * Delete reply by id
     * @param id id of reply
     * @throws ReplyNotFoundException If reply not found
     */
    public void removeReplyById(Long id) throws ReplyNotFoundException {
        getReplyById(id);
        replyRepository.deleteById(id);
    }

    /**
     * Update reply by id. If reply not found, it will be created.
     * @param replyId id of reply
     * @param reply Reply to update
     * @return id of updated reply
     */
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
    /**
     * Partial update reply by id. If reply not found, it will not be created.
     * @param postId id of reply
     * @param reply Reply to update
     * @return id of updated reply
     * @throws ReplyNotFoundException If reply not found
     */
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
