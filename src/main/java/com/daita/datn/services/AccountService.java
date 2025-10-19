package com.daita.datn.services;

import com.daita.datn.models.dto.auth.AuthenticationDTO;
import com.daita.datn.models.dto.auth.LoginRequestDTO;
import com.daita.datn.models.dto.auth.RegisterRequestDTO;
import com.nimbusds.jose.JOSEException;

import java.io.IOException;
import java.text.ParseException;

public interface AccountService {
    void register(RegisterRequestDTO requestDTO);
    AuthenticationDTO login(LoginRequestDTO requestDTO);
    void logout(String token) throws IOException, JOSEException, ParseException;
}
