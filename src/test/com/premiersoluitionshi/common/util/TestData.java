package com.premiersoluitionshi.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class TestData {
    private static final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz ";
    private static final String SALTCHARS_LINEBREAKS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz \n";

    public static String genRandString() {
        return genRandString(10);
    }

    public static String genRandString(int size) {
        return genRandString(size, SALTCHARS);
    }

    public static String genRandText(int size) {
        return genRandString(size, SALTCHARS_LINEBREAKS);
    }

    public static String genRandText() {
        return genRandText(1000);
    }

    public static String genRandString(int size, String saltChars) {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < size) { // length of the random string.
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        return salt.toString();
    }

    public static int genRandInt() {
        return genRandInt(10000); 
    }

    public static boolean genRandBoolean() {
        return genRandInt(1) == 1;
    }

    public static int genRandInt(int max) {
        return genRandInt(0, max); 
    }

    public static int genRandInt(int min, int max) {
        return (new Random()).nextInt(max + 1) + min; 
    }

    public static long genRandLong(int min, int max) {
        return (new Random()).nextLong() + min; 
    }

    public static LocalDate genRandDatePast() {
        return genRandDatePast(10);
    }

    public static LocalDateTime genRandDateTimeFuture() {
        LocalDate genRandDateFuture = genRandDateFuture(10);
        return addRandomizedTime(genRandDateFuture);
    }

    public static LocalDate genRandDate() {
        return genRandDatePast();
    }
    
    public static LocalDateTime genRandDateTime() {
        return genRandDateTimePast();
    }

    public static LocalDateTime genRandDateTimePast() {
        LocalDate genRandDateFuture = genRandDatePast(10);
        return addRandomizedTime(genRandDateFuture);
    }

    private static LocalDateTime addRandomizedTime(LocalDate genRandDateFuture) {
        int randHours = genRandInt(12);
        int randMin = genRandInt(59);
        int randSec = genRandInt(59);
        return LocalDateTime.of(genRandDateFuture, LocalTime.of(randHours, randMin, randSec));
    }

    public static LocalDate genRandDateFuture() {
        return genRandDateFuture(10);
    }

    public static LocalDate genRandDateFuture(int maxDays) {
        int genRandInt = genRandInt(maxDays);
        return LocalDate.now().plusDays(genRandInt);
    }

    public static LocalDate genRandDatePast(int maxDays) {
        int genRandInt = genRandInt(maxDays);
        return LocalDate.now().minusDays(genRandInt);
    }

    public static void main(String[] args) {
        System.out.println(genRandString(10));
        
    }
}
