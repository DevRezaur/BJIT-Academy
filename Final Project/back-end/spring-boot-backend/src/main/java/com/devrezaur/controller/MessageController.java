package com.devrezaur.controller;

import com.devrezaur.model.Message;
import com.devrezaur.model.Post;
import com.devrezaur.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/{batchId}")
    public ResponseEntity<?> getAllMessages(@PathVariable int batchId) {
        List<Message> messageList = messageService.getAllMessageByBatchId(batchId);

        if (messageList.size() == 0)
            return ResponseEntity.status(404).body("No Message Found");

        return ResponseEntity.status(200).body(messageList);
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping
    public ResponseEntity<?> getAllPosts(@RequestBody Message message) {
        if (messageService.createMessage(message))
            return ResponseEntity.status(201).body("Successfully Messaged");

        return ResponseEntity.status(400).body("Failed to Message");
    }
}
