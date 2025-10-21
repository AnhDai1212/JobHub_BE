package com.daita.datn.common.utils;

import jakarta.persistence.criteria.*;
import java.security.SecureRandom;
import java.util.stream.Collectors;

public class Util {
    private static final String NUMBERS = "0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String randomNumbers(int length){
        return SECURE_RANDOM.ints(length,0,NUMBERS.length())
                .mapToObj(NUMBERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}