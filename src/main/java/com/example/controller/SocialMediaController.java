package com.example.controller;

import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ConflictException;
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
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        try{
            Account registeredAccount = accountService.registerAccount(account);
            return ResponseEntity.ok().body(registeredAccount);
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch(ConflictException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        String username = account.getUsername();
        String password = account.getPassword();
        Account loggedAccount = accountService.authenticate(username, password);

        if(loggedAccount != null){
            return ResponseEntity.ok(loggedAccount);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        try{
            Message createdMessage = messageService.createMessage(message);
            return ResponseEntity.status(200).body(createdMessage);
        } catch(IllegalArgumentException e){
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.ok(messageService.getAllMessage());
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable("messageId") int messageId){
        Optional<Message> message = messageService.getMessageById(messageId);
        
        if(message.isPresent()){
            return ResponseEntity.ok(message.get());
        } else{
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteById(@PathVariable("messageId") int messageId){
        int rowAffected = messageService.deleteById(messageId);
        if(rowAffected == 1){
            return ResponseEntity.ok(rowAffected);
        } else{
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateById(@PathVariable("messageId") int messageId, @RequestBody Message message){
        int rowAffected = messageService.updateMessageById(messageId, message);
        if(rowAffected == 1){
            return ResponseEntity.ok(rowAffected);
        } else{
            return ResponseEntity.status(400).build();
        }
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessageByUserId(@PathVariable("accountId") int accountId){
        List<Message> messageByUser = messageService.getAllMessagesByUserId(accountId);
        return ResponseEntity.ok(messageByUser);
    }

}
