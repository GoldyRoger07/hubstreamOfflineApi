package com.hubstream.api.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
   private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//    private static final int CODE_LENGTH = 10;

   public static String generateRandomString(int CODE_LENGTH){
        StringBuilder stringBuilder = new StringBuilder(CODE_LENGTH);
        Random random = new Random();

        for(int i=0;i< CODE_LENGTH;i++){
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
   }
}
