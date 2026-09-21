package util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceUtil {

    private static ResourceBundle bundles;

    public static ResourceBundle setBundle(String key, Locale locale) {
        return bundles = ResourceBundle.getBundle(key,locale);
    }

    public static String getBundle(String message) {
        return bundles.getString(message);
    }
}
