package axal25.oles.jacek.util;

public class ExceptionUtils {

    public static String getUsefulMessage(Exception e) {
        if (e == null) {
            throw new IllegalArgumentException(Exception.class.getSimpleName() + " argument cannot be null.");
        }
        String usefulMessage = e.getLocalizedMessage();
        if (StringUtils.isNullOrEmptyOrBlank(usefulMessage)) {
            usefulMessage = e.getMessage();
        }
        if (StringUtils.isNullOrEmptyOrBlank(usefulMessage)) {
            usefulMessage = e.toString();
        }
        if (StringUtils.isNullOrEmptyOrBlank(usefulMessage) || e.getClass().getName().equals(usefulMessage)) {
            usefulMessage = e.getClass().getSimpleName();
        }
        return usefulMessage;
    }
}
