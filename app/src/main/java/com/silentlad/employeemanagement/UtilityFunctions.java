package com.silentlad.employeemanagement;

import java.util.Random;

public class UtilityFunctions {

    public UtilityFunctions(){}

    public String random(int maxLength){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        char tempChar;
        for (int i = 0; i < maxLength; i++) {
            tempChar = (char) (random.nextInt(25) + 97);
            sb.append(tempChar);
        }
        return sb.toString();
    }

    // TODO: CREATE FUNCTION TO GET DAYS FROM BOOLEAN VALUES

}
