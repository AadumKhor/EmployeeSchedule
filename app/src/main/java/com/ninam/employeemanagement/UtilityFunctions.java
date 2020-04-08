package com.ninam.employeemanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class UtilityFunctions {

    public UtilityFunctions(){}

    public String random(int maxLength) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        char tempChar;
        for (int i = 0; i < maxLength; i++) {
            tempChar = (char) (random.nextInt(25) + 97);
            sb.append(tempChar);
        }
        return sb.toString();
    }

    public ArrayList<String> getDaysFromInt(int [] days){
        ArrayList<String> result = new ArrayList<>();
        HashMap<String, Integer> map = new HashMap<>();

        map.put("sunday", days[0]);
        map.put("monday", days[1]);
        map.put("tuesday", days[2]);
        map.put("wednesday", days[3]);
        map.put("thursday", days[4]);
        map.put("friday", days[5]);
        map.put("saturday", days[6]);

        for(HashMap.Entry<String,Integer> current : map.entrySet()){
            if(current.getValue() == 1){
                result.add(current.getKey());
            }
        }
        return result;
    }

}
