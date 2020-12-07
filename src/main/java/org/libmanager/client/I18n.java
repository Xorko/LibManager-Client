package org.libmanager.client;

import javafx.beans.property.SimpleObjectProperty;

import java.util.*;

public final class I18n {

    private static ResourceBundle bundle;
    private static String baseName;
    private static final SimpleObjectProperty<Locale> locale;

    static {
        baseName = "strings";
        locale = new SimpleObjectProperty<>(getDefaultLocale());
        // When a new locale is selected, set it as default for the current instance of the JVM
        locale.addListener((observable, oldValue, newValue) -> Locale.setDefault(newValue));
        bundle = ResourceBundle.getBundle("strings", getDefaultLocale());
    }

    /**
     * Get supported locales
     * @return  A list with supported locales
     */
    public static List<Locale> getSupportedLocales() {
        return new ArrayList<>(Arrays.asList(Locale.FRANCE, Locale.US));
    }

    /**
     * Return the default locale, if it is not in supported locales
     * @return The default locale if it is supported, the US locale otherwise
     */
    public static Locale getDefaultLocale() {
        Locale sysDefault = Locale.getDefault();
        return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.US;
    }

    /**
     * Get the current locale
     * @return  The current locale
     */
    public static Locale getLocale() {
        return locale.get();
    }

    /**
     * Set the current locale
     * @param newLocale The new locale
     */
    public static void setLocale(Locale newLocale) {
        locale.set(newLocale);
        Locale.setDefault(newLocale);
    }

    /**
     * Return the locale property
     * @return the locale property
     */
    public static SimpleObjectProperty<Locale> localeProperty() {
        return locale;
    }

    /**
     * Get the currently loaded bundle
     * @return The currently loaded bundle
     */
    public static ResourceBundle getBundle() {
        return bundle;
    }

    /**
     * Load another bundle
     * @param baseName  The basename of the bundle
     * @param locale    The locale of the bundle
     */
    public static void setBundle(String baseName, Locale locale) {
        I18n.baseName = baseName;
        I18n.bundle = ResourceBundle.getBundle(baseName, locale);
    }

    /**
     * Load the same bundle but with another locale
     * @param locale The new locale to load
     */
    public static void changeLanguage(Locale locale) {
        setLocale(locale);
        bundle = ResourceBundle.getBundle(baseName, locale);
    }

}
