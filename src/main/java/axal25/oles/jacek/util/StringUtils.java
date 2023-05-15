package axal25.oles.jacek.util;

public class StringUtils {

    public static String trim(String input) {
        return input != null ? input.trim() : null;
    }

    public static boolean isNullOrEmptyOrBlank(final String input) {
        final String trimmedInput = trim(input);
        return trimmedInput == null || trimmedInput.isEmpty() || trimmedInput.isBlank();
    }
}
