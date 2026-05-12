package vsb.cz.fei.donkeykongfx;

import java.util.Locale;
import java.util.ResourceBundle;

public final class I18n {
    private static final String BUNDLE_NAME = "messages";
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());

    private I18n() {
    }

    public static ResourceBundle bundle() {
        return BUNDLE;
    }

    public static String get(String key) {
        return BUNDLE.getString(key);
    }
}
