package com.mibr.store.util;

public class NumberConverter{

    public static String convertToArabicNumerals(String input) {
        return input.replaceAll("0", "٠")
                .replaceAll("1", "١")
                .replaceAll("2", "٢")
                .replaceAll("3", "٣")
                .replaceAll("4", "٤")
                .replaceAll("5", "٥")
                .replaceAll("6", "٦")
                .replaceAll("7", "٧")
                .replaceAll("8", "٨")
                .replaceAll("9", "٩");
    }
}