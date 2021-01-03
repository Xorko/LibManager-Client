package org.libmanager.client;

import org.libmanager.client.enums.BookGenre;
import org.libmanager.client.enums.DVDGenre;
import org.libmanager.client.model.Book;
import org.libmanager.client.model.DVD;
import org.libmanager.client.model.Reservation;
import org.libmanager.client.model.User;
import org.libmanager.client.util.DateUtil;
import org.libmanager.server.response.AuthenticatedUser;

import java.time.LocalDate;

public class TestData {
    private static final User USER = new User("username", "firstName", "lastName", "address", "email",
            DateUtil.parse("01/01/2001"), DateUtil.parse("02/02/2002"), "token", true);
    private static final User USER_1 = new User("username1", "firstName1", "lastName1", "address1",
            "email1", DateUtil.parse("18/18/2018"), DateUtil.parse("20/20/2020"),
            "token1", false);

    private static final DVD DVD = new DVD(1, "dvd", "author", DVDGenre.ADVENTURE, LocalDate.EPOCH, 1, 1, "1h20");
    private static final DVD DVD_1 = new DVD(2, "dvd1", "author", DVDGenre.CRIMINAL, LocalDate.EPOCH, 2, 3, "2h20");

    private static final Book BOOK = new Book(1, "book", "author", BookGenre.BIOGRAPHY.toString(), LocalDate.EPOCH, 1, 2, "publisher", "qwerty");
    private static final Book BOOK_1 = new Book(2, "book1", "author", BookGenre.BIOGRAPHY.toString(), LocalDate.EPOCH, 0, 6, "publisher1", "azerty");

    private static final AuthenticatedUser INVALID_USER = new AuthenticatedUser(false, null, null, false, null, null);
    private static final AuthenticatedUser VALID_USER = new AuthenticatedUser(true, "username", "token", false, LocalDate.EPOCH.toString(), LocalDate.EPOCH.toString());

    private static final Reservation RESERVATION = new Reservation(1, "username", "book", "Book");
    private static final Reservation RESERVATION_1 = new Reservation(2, "username1", "dvd", "Dvd");

    public static User getUser() {
        return USER;
    }

    public static User getUser1() {
        return USER_1;
    }

    public static DVD getDvd() {
        return DVD;
    }

    public static DVD getDvd1() {
        return DVD_1;
    }

    public static Book getBook() {
        return BOOK;
    }

    public static Book getBook1() {
        return BOOK_1;
    }

    public static AuthenticatedUser getInvalidUser() {
        return INVALID_USER;
    }

    public static AuthenticatedUser getValidUser() {
        return VALID_USER;
    }

    public static Reservation getReservation() {
        return RESERVATION;
    }

    public static Reservation getReservation1() {
        return RESERVATION_1;
    }
}
