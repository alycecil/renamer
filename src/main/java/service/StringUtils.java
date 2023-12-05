package service;

public class StringUtils {
    public static boolean isBlank(String text) {
        return text == null || text.trim().equals("");
    }
}
