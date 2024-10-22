package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AccountRepository accountRepository;

    public Message createMessage(Message message) throws IllegalArgumentException{
        if(!accountRepository.existsById(message.getPostedBy())){
            throw new IllegalArgumentException("User not found.");
        }
        if(message.getMessageText() == null || message.getMessageText().isEmpty()){
            throw new IllegalArgumentException("Message cannot be empty.");
        }
        if(message.getMessageText().length() > 255){
            throw new IllegalArgumentException("Message cannot be over 255 character.");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessage(){
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int id){
        return messageRepository.findById(id);
    }

    public int deleteById(int messageId){
        try{
            messageRepository.deleteById(messageId);
            return 1;
        } catch(EmptyResultDataAccessException e){
            return 0;
        }
    }

    public int updateMessageById(int messageId, Message message){
        if(message.getMessageText().length() > 255 || message.getMessageText() == null || message.getMessageText().isEmpty()){
            return 0;
        }

        Optional<Message> currentMessage = messageRepository.findById(messageId);

        if(currentMessage.isPresent()){
            Message temp = currentMessage.get();
            temp.setMessageText(message.getMessageText());
            messageRepository.save(temp);
            return 1;
        } else{
            return 0;
        }
    }

    public List<Message> getAllMessagesByUserId(int userId){
        return messageRepository.findByPostedBy(userId);
    }

}
