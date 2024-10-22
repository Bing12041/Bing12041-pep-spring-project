package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.ConflictException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) throws IllegalArgumentException, ConflictException {
        //Invalid username and password
        if(account.getUsername() == null || account.getUsername().isEmpty()){
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        if(account.getPassword() == null || account.getPassword().length() < 4){
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }
        //Checks duplicate username
        if(accountRepository.findByUsername(account.getUsername()) != null){
            throw new ConflictException("The username already exist, please try again.");
        }
        return accountRepository.save(account);
    }


    public Account authenticate(String username, String password){
        Account account = accountRepository.findByUsername(username);
        if(account != null && account.getPassword().equals(password)){
            return account;
        }
        return null;
    }
}
