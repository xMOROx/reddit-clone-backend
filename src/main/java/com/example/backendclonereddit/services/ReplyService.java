package com.example.backendclonereddit.services;

import com.example.backendclonereddit.entities.Post;
import com.example.backendclonereddit.entities.Reply;
import com.example.backendclonereddit.repositories.ReplyRepository;
import com.example.backendclonereddit.utils.exceptions.types.ReplyNotFoundException;
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

    public void remove(Long id) {
        replyRepository.deleteById(id);
    }

    public Long fullUpdate(Long replyId, Reply reply) {
        Reply replyToUpdate;

        try {
            replyToUpdate = getReplyById(replyId);
        } catch (ReplyNotFoundException e) {
            createNewReply(reply);
            replyToUpdate = reply;
        }

        replyToUpdate.setId(replyId);
        replyToUpdate.setContent(reply.getContent());
        replyToUpdate.setUser(reply.getUser());
        replyToUpdate.setVotes(reply.getVotes());
        replyToUpdate.setCreatedDate(reply.getCreatedDate());
        replyToUpdate.setLastModifiedDate(reply.getLastModifiedDate());

        replyRepository.save(replyToUpdate);

        return replyId;
    }

    public Long partialUpdate(Long postId, Reply reply) {
        Reply replyToUpdate = getReplyById(postId);

        if(reply.getParentComment() != null) {
            replyToUpdate.setParentComment(reply.getParentComment());
        }

        if(reply.getContent() != null) {
            replyToUpdate.setContent(reply.getContent());
        }

        if(reply.getUser() != null) {
            replyToUpdate.setUser(reply.getUser());
        }

        if(reply.getVotes() != null) {
            replyToUpdate.setVotes(reply.getVotes());
        }

        if(reply.getCreatedDate() != null) {
            replyToUpdate.setCreatedDate(reply.getCreatedDate());
        }

        if(reply.getLastModifiedDate() != null) {
            replyToUpdate.setLastModifiedDate(reply.getLastModifiedDate());
        }
        replyRepository.save(replyToUpdate);

        return postId;
    }


}
