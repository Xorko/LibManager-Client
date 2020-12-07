package org.libmanager.client.util;

import javafx.util.StringConverter;
import org.libmanager.client.I18n;
import org.libmanager.client.enums.*;

public final class Converter {

    private static final StringConverter<Genre>     genreConverter;
    private static final StringConverter<BookGenre> bookGenreConverter;
    private static final StringConverter<DVDGenre>  dvdGenreConverter;
    private static final StringConverter<Status>    statusConverter;
    private static final StringConverter<Settings>  settingConverter;
    private static final StringConverter<Language>  languageConverter;

    static {
        genreConverter = new StringConverter<>() {
            @Override
            public String toString(Genre object) {
                if (object.toString().equals(BookGenre.POETRY.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.poetry");
                }
                if (object.toString().equals(BookGenre.NOVEL.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.novel");
                }
                if (object.toString().equals(BookGenre.SCIENCEFICTION.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.sciencefiction");
                }
                if (object.toString().equals(BookGenre.FANTASY.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.fantasy");
                }
                if (object.toString().equals(BookGenre.FANTASTIC.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.fantastic");
                }
                if (object.toString().equals(BookGenre.BIOGRAPHY.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.biography");
                }
                if (object.toString().equals(BookGenre.TALE.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.tale");
                }
                if (object.toString().equals(BookGenre.COMICSTRIP.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.comicstrip");
                }
                if (object.toString().equals(BookGenre.DRAMA.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.drama");
                }
                if (object.toString().equals(BookGenre.ESSAY.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.essay");
                }
                if (object.toString().equals(BookGenre.CRIMINAL.toString())) {
                    return I18n.getBundle().getString("enum.bookgenre.criminal");
                }
                if (object.toString().equals(DVDGenre.SCIENCEFICTION.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.sciencefiction");
                }
                if (object.toString().equals(DVDGenre.ADVENTURE.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.adventure");
                }
                if (object.toString().equals(DVDGenre.ADVENTURE.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.adventure");
                }
                if (object.toString().equals(DVDGenre.WESTERN.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.western");
                }
                if (object.toString().equals(DVDGenre.DOCUMENTARY.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.documentary");
                }
                if (object.toString().equals(DVDGenre.SPYING.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.spying");
                }
                if (object.toString().equals(DVDGenre.SUPERHERO.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.superhero");
                }
                if (object.toString().equals(DVDGenre.CRIMINAL.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.criminal");
                }
                if (object.toString().equals(DVDGenre.DRAMA.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.drama");
                }
                if (object.toString().equals(DVDGenre.FANTASY.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.fantasy");
                }
                if (object.toString().equals(DVDGenre.FANTASTIC.toString())) {
                    return I18n.getBundle().getString("enum.dvdgenre.fantastic");
                }
                return null;
            }

            @Override
            public Genre fromString(String string) {
                return null;
            }
        };

        bookGenreConverter = new StringConverter<>() {
            @Override
            public String toString(BookGenre object) {
                switch (object) {
                    case ANY:
                        return I18n.getBundle().getString("enum.genre.any");
                    case POETRY:
                        return I18n.getBundle().getString("enum.bookgenre.poetry");
                    case NOVEL:
                        return I18n.getBundle().getString("enum.bookgenre.novel");
                    case SCIENCEFICTION:
                        return I18n.getBundle().getString("enum.bookgenre.sciencefiction");
                    case FANTASY:
                        return I18n.getBundle().getString("enum.bookgenre.fantasy");
                    case FANTASTIC:
                        return I18n.getBundle().getString("enum.bookgenre.fantastic");
                    case BIOGRAPHY:
                        return I18n.getBundle().getString("enum.bookgenre.biography");
                    case TALE:
                        return I18n.getBundle().getString("enum.bookgenre.tale");
                    case COMICSTRIP:
                        return I18n.getBundle().getString("enum.bookgenre.comicstrip");
                    case DRAMA:
                        return I18n.getBundle().getString("enum.bookgenre.drama");
                    case ESSAY:
                        return I18n.getBundle().getString("enum.bookgenre.essay");
                    case CRIMINAL:
                        return I18n.getBundle().getString("enum.bookgenre.criminal");
                    default:
                        break;
                }
                return null;
            }

            @Override
            public BookGenre fromString(String string) {
                return null;
            }
        };

        dvdGenreConverter = new StringConverter<>() {
            @Override
            public String toString(DVDGenre object) {
                switch (object) {
                    case ANY:
                        return I18n.getBundle().getString("enum.genre.any");
                    case SCIENCEFICTION:
                        return I18n.getBundle().getString("enum.dvdgenre.sciencefiction");
                    case ADVENTURE:
                        return I18n.getBundle().getString("enum.dvdgenre.adventure");
                    case WESTERN:
                        return I18n.getBundle().getString("enum.dvdgenre.western");
                    case DOCUMENTARY:
                        return I18n.getBundle().getString("enum.dvdgenre.documentary");
                    case SPYING:
                        return I18n.getBundle().getString("enum.dvdgenre.spying");
                    case SUPERHERO:
                        return I18n.getBundle().getString("enum.dvdgenre.superhero");
                    case CRIMINAL:
                        return I18n.getBundle().getString("enum.dvdgenre.criminal");
                    case DRAMA:
                        return I18n.getBundle().getString("enum.dvdgenre.drama");
                    case FANTASY:
                        return I18n.getBundle().getString("enum.dvdgenre.fantasy");
                    case FANTASTIC:
                        return I18n.getBundle().getString("enum.dvdgenre.fantastic");
                    default:
                        break;
                }
                return null;
            }

            @Override
            public DVDGenre fromString(String string) {
                return null;
            }
        };

        statusConverter = new StringConverter<>() {
            @Override
            public String toString(Status object) {
                switch (object) {
                    case AVAILABLE:
                        return I18n.getBundle().getString("enum.status.available");
                    case UNAVAILABLE:
                        return I18n.getBundle().getString("enum.status.unavailable");
                    default:
                        break;
                }
                return null;
            }

            @Override
            public Status fromString(String string) {
                return null;
            }
        };

        settingConverter = new StringConverter<>() {
            @Override
            public String toString(Settings object) {
                if (object.getKey().equals(GlobalSettings.GENERAL.getKey())) {
                    return I18n.getBundle().getString("enum.settings.global.general");
                }
                if (object.getKey().equals(AdminSettings.SERVER.getKey())) {
                    return I18n.getBundle().getString("enum.settings.admin.server");
                }
                if (object.getKey().equals(AdminSettings.MAIL.getKey())) {
                    return I18n.getBundle().getString("enum.settings.admin.mail");
                }
                return null;
            }

            @Override
            public Settings fromString(String string) {
                return null;
            }
        };

        languageConverter = new StringConverter<>() {
            @Override
            public String toString(Language object) {
                switch (object) {
                    case en_US:
                        return I18n.getBundle().getString("enum.language.english.us");
                    case fr_FR:
                        return I18n.getBundle().getString("enum.language.francais.france");
                    default:
                        break;
                }
                return null;
            }

            @Override
            public Language fromString(String string) {
                return null;
            }
        };
    }

    /**
     * Return the StringConverter for Genre enums
     * @return The StringConverter for Genre enums
     */
    public static StringConverter<Genre> getGenreConverter() {
        return genreConverter;
    }

    /**
     * Return the StringConverter for the BookGenre enum
     * @return the StringConverter for the BookGenre enum
     */
    public static StringConverter<BookGenre> getBookGenreConverter() {
        return bookGenreConverter;
    }

    /**
     * Return the StringConverter for the DVDGenre enum
     * @return the StringConverter for the DVDGenre enum
     */
    public static StringConverter<DVDGenre> getDvdGenreConverter() {
        return dvdGenreConverter;
    }

    /**
     * Return the StringConverter for the Status enum
     * @return the StringConverter for the Status enum
     */
    public static StringConverter<Status> getStatusConverter() {
        return statusConverter;
    }

    /**
     * Return the StringConverter for Settings enums
     * @return the StringConverter for Settings enums
     */
    public static StringConverter<Settings> getSettingConverter() {
        return settingConverter;
    }

    /**
     * Return the StringConverter for Language enum
     * @return The StringConverter for Language enum
     */
    public static StringConverter<Language> getLanguageConverter() {
        return languageConverter;
    }

}
