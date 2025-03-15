package com.reporthub.entity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public interface IModel {
    default String generateKey(Object classObject) {

        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 25;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return classObject.getClass().getSimpleName().toLowerCase().substring(0, 3) + "_" + generatedString;
    }

    public default Date generateDate() {
        return Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
