package com.devrezaur.service;

import com.devrezaur.model.Message;
import com.devrezaur.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getAllMessageByBatchId(int batchId) {
        return messageRepository.findAllByBatchId(batchId);
    }

    public boolean createMessage(Message message) {
        return (messageRepository.save(message) != null) ? true : false;
    }
}
