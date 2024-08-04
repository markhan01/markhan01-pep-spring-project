package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
@Transactional
public class MessageService {

    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessage(int messageId) {
        return messageRepository.findById(messageId);
    }

    public void deleteMessage(int messageId) {
        messageRepository.deleteById(messageId);
    }

    public List<Message> findAllByPostedBy(int postedBy) {
        return messageRepository.findAllByPostedBy(postedBy);
    }

}
