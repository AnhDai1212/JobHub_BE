package com.daita.datn.services;

public interface PasswordService {
    String encryptPassword(String password);
    boolean matches(String password, String encodedPassword);
}
