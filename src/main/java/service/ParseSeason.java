package service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseSeason {
    static Pattern pattern = Pattern.compile("(\\d+) *$");

    public static Integer parse(String f) {
        // Rip .+(\d+) -> $1
        Matcher matcher = pattern.matcher(f);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }
}
