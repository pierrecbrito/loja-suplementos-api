package com.suplementos.lojasuplementosapi.core;


public final class StringUtils {
    private StringUtils() {}
    
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return null;
        }
        
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }
    
    public static String maskEmail(String email) {
        if (isEmpty(email)) {
            return email;
        }
        
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) {
            return email;
        }
        
        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        
        int usernameLength = username.length();
        int visibleChars = Math.min(3, usernameLength);
        int maskedChars = usernameLength - visibleChars;
        
        StringBuilder maskedUsername = new StringBuilder(username.substring(0, visibleChars));
        for (int i = 0; i < maskedChars; i++) {
            maskedUsername.append("*");
        }
        
        return maskedUsername.append(domain).toString();
    }
    
    public static String formatCpf(String cpf) {
        if (isEmpty(cpf) || cpf.length() != 11) {
            return cpf;
        }
        
        return cpf.substring(0, 3) + "." + 
               cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + 
               cpf.substring(9);
    }
    
    public static String formatPhone(String phone) {
        if (isEmpty(phone)) {
            return phone;
        }
        
        if (phone.length() == 10) {
            return "(" + phone.substring(0, 2) + ") " + 
                   phone.substring(2, 6) + "-" + 
                   phone.substring(6);
        } else if (phone.length() == 11) {
            return "(" + phone.substring(0, 2) + ") " + 
                   phone.substring(2, 7) + "-" + 
                   phone.substring(7);
        }
        
        return phone;
    }
}