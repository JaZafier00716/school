package vsb.cz.fei.donkeykongfx;

import java.util.Locale;
import java.util.ResourceBundle;

public final class I18n {
    private static final String BUNDLE_NAME = "messages";
    private static Locale locale = Locale.getDefault();
    private static ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);

    private I18n() {
    }

    public static ResourceBundle bundle() {
        return bundle;
    }

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static Locale locale() {
        return locale;
    }

    public static void setLocaleTag(String languageTag) {
        Locale selectedLocale = languageTag == null || languageTag.isBlank()
                ? Locale.ENGLISH
                : Locale.forLanguageTag(languageTag);
        Locale.setDefault(selectedLocale);
        locale = selectedLocale;
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }
}
