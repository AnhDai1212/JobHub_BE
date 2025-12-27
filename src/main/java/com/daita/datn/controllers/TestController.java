package com.daita.datn.controllers;

import com.daita.datn.models.entities.auth.Account;
import com.daita.datn.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final AccountRepository accountRepository;
    @GetMapping("test1")
    public String test() {
//        Account account = new Account();
//        account.setEmail("test@example.com");
//        account.setPassword("123456");
//
//        accountRepository.save(account);
//
        return "test";
    }
}
