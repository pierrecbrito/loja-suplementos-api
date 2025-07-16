package com.suplementos.lojasuplementosapi.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {
    private DateUtils() {}
    
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMATTER.format(date);
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return DATE_TIME_FORMATTER.format(dateTime);
    }
    
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }
    
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }
    
    public static boolean isDateValid(String dateString) {
        try {
            parseDate(dateString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isDateTimeValid(String dateTimeString) {
        try {
            parseDateTime(dateTimeString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}