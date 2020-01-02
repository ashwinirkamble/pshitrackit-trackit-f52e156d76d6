package com.premiersoluitionshi.common;

import java.util.ArrayList;

import edu.emory.mathcs.backport.java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String userDotName = System.getProperty("user.name");
        System.out.println("|||||||||||||| userDotName=" + userDotName);

        String searchText = "Lewis Nakao";
        @SuppressWarnings("unchecked")
        ArrayList<String> searchTerms = new ArrayList<>(Arrays.asList(searchText.split("\\s+")));
        for (String string : searchTerms) {
            System.out.println("string=" + string);
        }
    }
}
