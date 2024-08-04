package com.example.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerAccount(@RequestBody Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return ResponseEntity.status(400).body("Invalid username or password");
        }

        if (accountService.findByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(409).body("Duplicate username");
        }

        Account addedAccount = accountService.addAccount(account);
        return ResponseEntity.status(200).body(addedAccount);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        Account loginAccount = accountService.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (loginAccount == null) {
            return ResponseEntity.status(401).body("Incorrect credentials");
        }
        return ResponseEntity.status(200).body(loginAccount);
    }

    @PostMapping(value = "/messages")
    public ResponseEntity<?> postMessage(@RequestBody Message message) {
        if (message.getMessageText().isBlank() || message.getMessageText().length() > 254) {
            return ResponseEntity.status(400).body("Invalid message");
        }

        if (accountService.findById(message.getPostedBy()).isEmpty()) {
            return ResponseEntity.status(400).body("User does not exist");
        }

        Message addedMessage = messageService.addMessage(message);
        return ResponseEntity.status(200).body(addedMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages() {
        return ResponseEntity.status(200).body(messageService.getAllMessages());
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<?> getMessage(@PathVariable("message_id") int messageId) {
        Optional<Message> addedMessage = messageService.getMessage(messageId);
        if (addedMessage.isEmpty()) return ResponseEntity.status(200).body("");
        return ResponseEntity.status(200).body(addedMessage.get());
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("message_id") int messageId) {
        if (messageService.getMessage(messageId).isPresent()) {
            messageService.deleteMessage(messageId);
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(200).body("");
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<?> patchMessage(@PathVariable("message_id") int messageId, @RequestBody Message message) {
        if (messageService.getMessage(messageId).isEmpty()) {
            return ResponseEntity.status(400).body("Message does not exist");
        }

        if (message.getMessageText().isBlank() || message.getMessageText().length() > 254) {
            return ResponseEntity.status(400).body("Invalid message");
        }

        messageService.addMessage(message);
        return ResponseEntity.status(200).body(1);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<?> getAllMessagesByAccountId(@PathVariable("account_id") int accountId) {
        return ResponseEntity.status(200).body(messageService.findAllByPostedBy(accountId));
    }

}
