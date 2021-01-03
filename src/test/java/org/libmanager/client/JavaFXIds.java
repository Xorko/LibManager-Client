package org.libmanager.client;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import org.libmanager.client.model.Reservation;

public class JavaFXIds {

    //Login Window
    public final static String USERNAME_FIELD = "#username";
    public final static String PASSWORD_FIELD = "#password";
    public final static String LOGIN_BUTTON = "#login";
    public final static String ERROR_MESSAGE = "#errorMessage";

    //Admin Window
    public static final String ADMIN_TAB_PANE = "#tabPane";

    //Item Window
    public static final String ITEMS_TAB_PANE = "#itemsTabPane";

    //Book Tab
    public static final String BOOKS_TABLE = "#booksTable";
    //Book Search
    public static final String SEARCH_BOOK_TITLE = "#bookTitleField";
    public static final String SEARCH_BOOK_AUTHOR = "#bookAuthorField";
    public static final String SEARCH_BOOK_PUBLISHER = "#bookPublisherField";
    public static final String SEARCH_BOOK_GENRE = "#bookGenreCBox";
    public static final String SEARCH_BOOK_RELEASE_DATE = "#bookReleaseDateDPicker";
    public static final String SEARCH_BOOK_ISBN = "#bookIsbnField";
    public static final String SEARCH_BOOK_STATUS = "#bookStatusCBox";

    public static final String BOOK_SEARCH_BUTTON = "#bookSearch";
    public static final String BOOK_RESET_BUTTON = "#bookReset";
    //Book List Edit
    public static final String ADD_BOOK = "#addBook";
    public static final String DELETE_BOOK = "#deleteBook";
    public static final String EDIT_BOOK = "#editBook";

    //Dvd Tab
    public static final String DVD_TABLE = "#dvdTable";
    //Dvd Search
    public static final String SEARCH_DVD_TITLE = "#dvdTitleField";
    public static final String SEARCH_DVD_DIRECTOR = "#dvdDirectorField";
    public static final String SEARCH_DVD_GENRE = "#dvdGenreCBox";
    public static final String SEARCH_DVD_RELEASE_DATE = "#dvdReleaseDateDPicker";
    public static final String SEARCH_DVD_STATUS = "#dvdStatusCBox";

    public static final String DVD_SEARCH_BUTTON = "#dvdSearch";
    public static final String DVD_RESET_BUTTON = "#dvdReset";
    //Dvd list edit
    public static final String ADD_DVD = "#addDVD";
    public static final String DELETE_DVD = "#deleteDVD";
    public static final String EDIT_DVD = "#editDVD";

    //Users Window
    public static final String USERS_TABLE = "#usersTable";
    //User List Edit
    public static final String ADD_USER = "#userAddButton";
    public static final String DELETE_USER = "#userDeleteButton";
    public static final String EDIT_USER = "#userEditButton";

    //Admin Reservations Window
    public static final String RESERVATION_TABLE = "#reservationsTable";
    public static final String DELETE_RESERVATION_COLUMN = "#reservationDeleteColumn";



    //Reservation View
    //Book Tab
    public static final String RESERVATION_BOOKS_TABLE = "#booksTable";
    public static final String RESERVE_BOOK_COLUMN = "#bookStatusColumn";


    //Add Item
    public static final String ADD_TITLE = "#titleField";
    public static final String ADD_AUTHOR = "#authorField";
    public static final String ADD_GENRE = "#genreCBox";
    public static final String ADD_RELEASE_DATE = "#releaseDateDPicker";
    public static final String ADD_COPIES = "#copiesSpinner";
    public static final String CANCEL_ADD = "#cancelButton";
    public static final String RESET_ADD = "#resetButton";
    public static final String CONFIRM_ADD = "#confirmButton";
    //Add book
    public static final String ADD_PUBLISHER = "#publisherField";
    public static final String ADD_ISBN = "#isbnField";
    //Add dvd


}
