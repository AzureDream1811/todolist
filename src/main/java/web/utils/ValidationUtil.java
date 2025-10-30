package web.utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private ValidationUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validates if a string is null or empty (after trimming whitespace).
     *
     * @param str the string to validate
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Validates if an email address is in a valid format.
     *
     * @param email the email to validate
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if a password meets the minimum security requirements.
     *
     * @param password  the password to validate
     * @param minLength minimum required length
     * @return true if the password is valid, false otherwise
     */
    public static boolean isValidPassword(String password, int minLength) {
        return password != null && password.length() >= minLength;
        // Add more password strength requirements here if needed
    }

    /**
     * Validates that all provided strings are not null or empty.
     *
     * @param strings variable number of strings to validate
     * @return true if all strings are not null and not empty, false otherwise
     */
    public static boolean areAllNotEmpty(String... strings) {
        if (strings == null) {
            return true;
        }
        for (String str : strings) {
            if (isNullOrEmpty(str)) {
                return true;
            }
        }
        return false;
    }
}
