package org.libmanager.client.service;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.libmanager.client.util.Config;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Requests {

    private static String GET_ALL_BOOKS;
    private static String GET_ALL_DVDS;
    private static String GET_ALL_USERS;
    private static String GET_ALL_RESERVATIONS;
    private static String GET_BOOK;
    private static String GET_DVD;
    private static String GET_USER;
    private static String GET_RESERVATION;
    private static String GET_USER_RESERVATIONS;
    private static String SEARCH_BOOK;
    private static String SEARCH_DVD;
    private static String SEARCH_USER;
    private static String SEARCH_RESERVATION;
    private static String ADD_BOOK;
    private static String ADD_DVD;
    private static String ADD_USER;
    private static String ADD_RESERVATION;
    private static String EDIT_BOOK;
    private static String EDIT_DVD;
    private static String EDIT_USER;
    private static String DELETE_ITEM;
    private static String DELETE_USER;
    private static String DELETE_RESERVATION;
    private static String LOGIN;
    private static String RESET_PASSWORD;
    private static String CHECK_USERNAME;

    static {
        // Initialize all the endpoints
        String server = Config.getServerProtocol() + "://" + Config.getServerAddress()
                + ":" + Config.getServerPort();

        GET_ALL_BOOKS           = server + "/item/book/all";
        GET_ALL_DVDS            = server + "/item/dvd/all";
        GET_ALL_USERS           = server + "/user/all";
        GET_ALL_RESERVATIONS    = server + "/reservation/all";
        GET_BOOK                = server + "/item/book/get/";
        GET_DVD                 = server + "/item/dvd/get/";
        GET_USER                = server + "/user/get/";
        GET_RESERVATION         = server + "/reservation/get/";
        GET_USER_RESERVATIONS   = server + "/reservation/get_user_reservations";
        SEARCH_BOOK             = server + "/item/book/search";
        SEARCH_DVD              = server + "/item/dvd/search";
        SEARCH_USER             = server + "/user/search";
        SEARCH_RESERVATION      = server + "/reservation/search";
        ADD_BOOK                = server + "/item/book/add";
        ADD_DVD                 = server + "/item/dvd/add";
        ADD_USER                = server + "/user/add";
        ADD_RESERVATION         = server + "/reservation/add";
        EDIT_BOOK               = server + "/item/book/edit/";
        EDIT_DVD                = server + "/item/dvd/edit/";
        EDIT_USER               = server + "/user/edit/";
        DELETE_ITEM             = server + "/item/delete/";
        DELETE_USER             = server + "/user/delete/";
        DELETE_RESERVATION      = server + "/reservation/delete/";
        LOGIN                   = server + "/account/login";
        RESET_PASSWORD          = server + "/account/reset_password";
        CHECK_USERNAME          = server + "/user/check_username/";
    }

    /**
     * Call the GET_ALL_BOOKS endpoint
     * @return The server response
     */
    public static String callGetAllBooks() {
        URI uri = null;
        try {
            uri = new URIBuilder(GET_ALL_BOOKS).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    /**
     * Call the GET_ALL_DVD endpoint
     * @return The server response
     */
    public static String callGetAllDVDs() {
        URI uri = null;
        try {
            uri = new URIBuilder(GET_ALL_DVDS).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    /**
     * Call the GET_ALL_USERS
     * @param token The token of the logged in user (must be admin)
     * @return      The server response
     */
    public static String callGetAllUsers(String token) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        return sendPOST(GET_ALL_USERS, urlParameters);
    }

    /**
     * Call the GET_ALL_RESERVATIONS endpoint
     * @param token The token of the logged in user (must be admin)
     * @return      The server response
     */
    public static String callGetAllReservations(String token) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        return sendPOST(GET_ALL_RESERVATIONS, urlParameters);
    }

    /**
     * Get the book by its id
     * @param id The id of the book to get
     * @return   The server response
     */
    public static String callGetBook(String id) {
        URI uri = null;
        try {
            uri = new URIBuilder(GET_BOOK + id).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    /**
     * Get the DVD by its id
     * @param id The id of the DVD to get
     * @return   The server response
     */
    public static String callGetDVD(String id) {
        URI uri = null;
        try {
            uri = new URIBuilder(GET_DVD + id).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    /**
     * Get the user by its username
     * @param token     The token of the logged in user (must be admin)
     * @param username The username of the user to get
     * @return         The server response
     */
    public static String callGetUser(String token, String username) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        return sendPOST(GET_USER + username, urlParameters);
    }

    /**
     * Get the reservation by its id
     * @param token The token of the logged in user (must be admin)
     * @param id    The id of the reservation to get
     * @return      The server response
     */
    public static String callGetReservation(String token, String id) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        return sendPOST(GET_RESERVATION + id, urlParameters);
    }

    public static String callGetReservationsByUser(String token) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        return sendPOST(GET_USER_RESERVATIONS, urlParameters);
    }

    /**
     * Call the SEARCH_BOOK endpoint
     * @param isbn          The ISBN of the searched book
     * @param author        The author of the searched book
     * @param title         The title of the searched book
     * @param publisher     The publisher of the searched book
     * @param releaseDate   The release date of the searched book
     * @param genre         The genre of the searched book
     * @param status        The status of the searched book
     * @return              The server response
     */
    public static String callSearchBooks(String isbn, String author, String title, String publisher, String releaseDate, String genre, String status) {
        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(SEARCH_BOOK);
            if (isbn != null)        builder.addParameter("isbn", isbn);
            if (author != null)      builder.addParameter("author", author);
            if (title != null)       builder.addParameter("title", title);
            if (publisher != null)   builder.addParameter("publisher", publisher);
            if (releaseDate != null) builder.addParameter("releaseDate", releaseDate);
            if (genre != null)       builder.addParameter("genre", genre);
            if (status != null)      builder.addParameter("status", status);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    /**
     * Call the SEARCH_DVD endpoint
     * @param director      The director of the searched movie
     * @param title         The title of the searched movie
     * @param releaseDate   The release date of the searched movie
     * @param genre         The genre of the searched movie
     * @param status        The status of the searched movie
     * @return              The server response
     */
    public static String callSearchDVD(String director, String title, String releaseDate, String genre, String status) {
        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(SEARCH_DVD);
            if (director != null)    builder.addParameter("director", director);
            if (title != null)       builder.addParameter("title", title);
            if (releaseDate != null) builder.addParameter("releaseDate", releaseDate);
            if (genre != null)       builder.addParameter("genre", genre);
            if (status != null)      builder.addParameter("status", status);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    /**
     * Call the SEARCH_USER endpoint
     * @param token     The token of the logged in user (must be admin)
     * @param username  The username of the searched user
     * @param email     The email of the searched user
     * @param firstName The firstname of the searched user
     * @param lastName  The lastname of the searched user
     * @param birthday  The birthday of the searched user
     * @param address   The address of the searched user
     * @return          The server response
     */
    public static String callSearchUser(String token, String username, String email, String firstName, String lastName, String address, String birthday, String registrationDate) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        if (username != null)         urlParameters.add(new BasicNameValuePair("username", username));
        if (email != null)            urlParameters.add(new BasicNameValuePair("email", email));
        if (firstName != null)        urlParameters.add(new BasicNameValuePair("firstName", firstName));
        if (lastName != null)         urlParameters.add(new BasicNameValuePair("lastName", lastName));
        if (birthday != null)         urlParameters.add(new BasicNameValuePair("birthday", birthday));
        if (address != null)          urlParameters.add(new BasicNameValuePair("address", address));
        if (registrationDate != null) urlParameters.add(new BasicNameValuePair("registrationDate", registrationDate));
        return sendPOST(SEARCH_USER, urlParameters);
    }

    /**
     * Call the SEARCH_RESERVATION endpoint
     * @param token                 The token of the logged in user (must be admin)
     * @param id                    The id of the searched reservation
     * @param username              The username of the searched reservation
     * @param title                 The title of the searched reservation
     * @param reservationDate       The date of the reservation
     * @param itemType              The type of the borrowed item
     * @return                      The server response
     */
    public static String callSearchReservation(String token, String id, String username, String title, String itemType, String reservationDate) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        if (id != null)       urlParameters.add(new BasicNameValuePair("id", id));
        if (username != null) urlParameters.add(new BasicNameValuePair("username", username));
        if (title != null)    urlParameters.add(new BasicNameValuePair("title", title));
        if (itemType != null) urlParameters.add(new BasicNameValuePair("itemType", itemType));
        if (reservationDate != null) urlParameters.add(new BasicNameValuePair("reservationDate", reservationDate));
        return sendPOST(SEARCH_RESERVATION, urlParameters);
    }

    /**
     * Call the ADD_BOOK endpoint
     * @param token         The token of the logged in user (must be admin)
     * @param isbn          The isbn of the book to add to be added
     * @param author        The author of the book to add to be added
     * @param title         The title of the book to add to be added
     * @param publisher     The publisher of the book to add to be added
     * @param releaseDate   The release date of the book to add to be added
     * @param genre         The genre of the book to add to be added
     * @param copies        The number of copies of the book to be added
     * @return              The server response
     */
    public static String callAddBook(String token, String isbn, String author, String title,
                                     String publisher, String releaseDate, String genre, int copies) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("isbn", isbn));
        urlParameters.add(new BasicNameValuePair("author", author));
        urlParameters.add(new BasicNameValuePair("title", title));
        urlParameters.add(new BasicNameValuePair("publisher", publisher));
        urlParameters.add(new BasicNameValuePair("releaseDate", releaseDate));
        urlParameters.add(new BasicNameValuePair("genre", genre));
        urlParameters.add(new BasicNameValuePair("copies", Integer.toString(copies)));
        urlParameters.add(new BasicNameValuePair("totalCopies", Integer.toString(copies)));
        return sendPOST(ADD_BOOK, urlParameters);
    }

    /**
     * Call the ADD_DVD endpoint
     * @param token         The token of the logged in user (must be admin)
     * @param director      The director of the movie to be added
     * @param title         The title of the movie to be added
     * @param duration      The duration of the movie to be added
     * @param releaseDate   The release date of the movie to be added
     * @param genre         The genre of the dvd to be added
     * @param totalCopies   The number of copies of the DVD to be added
     * @return              The server response
     */
    public static String callAddDVD(String token, String director, String title, String duration, String releaseDate, String genre, int totalCopies) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("director", director));
        urlParameters.add(new BasicNameValuePair("title", title));
        urlParameters.add(new BasicNameValuePair("duration", duration));
        urlParameters.add(new BasicNameValuePair("releaseDate", releaseDate));
        urlParameters.add(new BasicNameValuePair("genre", genre));
        urlParameters.add(new BasicNameValuePair("totalCopies", Integer.toString(totalCopies)));
        return sendPOST(ADD_DVD, urlParameters);
    }

    /**
     * Call the ADD_USER endpoint
     * @param token     The token of the logged in user (must be admin)
     * @param username  The username of the user to be added
     * @param email     The email of the user to be added
     * @param password  The password of the user to be added
     * @param firstName The firstname of the user to be added
     * @param lastName  The lastname  of the user to be added
     * @param birthday  The birthday of the user to be added
     * @param address   The address of the user to be added
     * @return          The server response
     */
    public static String callAddUser(String token, String username, String email, String password, String firstName, String lastName, String birthday, String address) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("firstName", firstName));
        urlParameters.add(new BasicNameValuePair("lastName", lastName));
        urlParameters.add(new BasicNameValuePair("email", email));
        urlParameters.add(new BasicNameValuePair("password", password));
        urlParameters.add(new BasicNameValuePair("birthday", birthday));
        urlParameters.add(new BasicNameValuePair("address", address));
        return sendPOST(ADD_USER, urlParameters);
    }

    /**
     * Call the ADD_RESERVATION endpoint
     * @param token     The token of the logged in user
     * @param itemId    The id of the item the user wants to borrow
     * @return          The server response
     */
    public static String callAddReservation(String token, String itemId) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("itemId", itemId));
        return sendPOST(ADD_RESERVATION, urlParameters);
    }

    /**
     * Call the EDIT_BOOK endpoint
     * @param token         The token of the user (must be admin)
     * @param id            The id of the book to edit
     * @param isbn          The isbn of the book
     * @param author        The author of the book
     * @param title         The title of the book
     * @param publisher     The publisher of the book
     * @param releaseDate   The release date of the book
     * @param genre         The genre of the book
     * @param totalCopies   The number of copies of the DVD
     * @return              The server response
     */
    public static String callEditBook(String token, String id, String isbn, String author, String title, String publisher,
                                      String releaseDate, String genre, int totalCopies) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("isbn", isbn));
        urlParameters.add(new BasicNameValuePair("author", author));
        urlParameters.add(new BasicNameValuePair("title", title));
        urlParameters.add(new BasicNameValuePair("publisher", publisher));
        urlParameters.add(new BasicNameValuePair("releaseDate", releaseDate));
        urlParameters.add(new BasicNameValuePair("genre", genre));
        urlParameters.add(new BasicNameValuePair("totalCopies", Integer.toString(totalCopies)));
        return sendPOST(EDIT_BOOK + id, urlParameters);
    }

    /**
     * Call the EDIT_DVD endpoint
     @return              The server response
      * @param token         The token of the user (must be admin)
     * @param id            The id of the dvd to edit
     * @param author        The director of the movie
     * @param title         The title of the movie
     * @param duration      The duration of the movie
     * @param releaseDate   The release date of the movie
     * @param genre         The genre of the movie
     * @param totalCopies   The number of copies of the DVD
     */
    public static String callEditDVD(String token, String id, String author, String title, String duration,
                                     String releaseDate, String genre, int totalCopies) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("director", author));
        urlParameters.add(new BasicNameValuePair("title", title));
        urlParameters.add(new BasicNameValuePair("duration", duration));
        urlParameters.add(new BasicNameValuePair("releaseDate", releaseDate));
        urlParameters.add(new BasicNameValuePair("genre", genre));
        urlParameters.add(new BasicNameValuePair("totalCopies", Integer.toString(totalCopies)));
        return sendPOST(EDIT_DVD + id, urlParameters);
    }

    /**
     * Call the EDIT_USER endpoint
     * @param token     The token of the logged in user (must be admin)
     * @param username  The username of the user to edit
     * @param email     The new email of the user to edit
     * @param firstName The new firstname of the user to edit
     * @param lastName  The new lastname of the user to edit
     * @param birthday  The new birthday of the user to edit
     * @param address   The new address of the user to edit
     * @return          The server response
     */
    public static String callEditUser(String token, String username, String email, String firstName, String lastName, String birthday, String address) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("email", email));
        urlParameters.add(new BasicNameValuePair("firstName", firstName));
        urlParameters.add(new BasicNameValuePair("lastName", lastName));
        urlParameters.add(new BasicNameValuePair("birthday", birthday));
        urlParameters.add(new BasicNameValuePair("address", address));
        return sendPOST(EDIT_USER + username, urlParameters);
    }

    /**
     * Call the DELETE_ITEM endpoint
     * @param token The token of the logged in user (must be admin)
     * @param id    The id of the item to delete
     * @return      The server response
     */
    public static String callDeleteItem(String token, String id) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        return sendPOST(DELETE_ITEM + id, urlParameters);
    }

    /**
     * Call the DELETE_USER endpoint
     * @param token     The token of the logged in user (must be admin)
     * @param username  The username of the user to delete
     * @return          The server response
     */
    public static String callDeleteUser(String token, String username) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        return sendPOST(DELETE_USER + username, urlParameters);
    }

    public static String callDeleteReservation(String token, String id) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        return sendPOST(DELETE_RESERVATION + id, urlParameters);
    }

    /**
     * Call the LOGIN endpoint
     * @param username  The username of the user
     * @param password  The password of the user
     * @return          The server response
     */
    public static String callLogin(String username, String password) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("password", password));
        return sendPOST(LOGIN, urlParameters);
    }

    /**
     * Call the RESET_PASSWORD endpoint with GET method
     * @param username  The user who asks for a password reset
     * @return          The response of the server
     */
    public static String callGetResetPassword(String username) {
        URI uri = null;
        try {
            uri = new URIBuilder(RESET_PASSWORD).addParameter("username", username).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    /**
     * Call the RESET_PASSWORD endpoint with POST method
     * @param mailToken     The token sent by mail to the user
     * @param newPassword   The new password of the user
     * @return              The response of the server
     */
    public static String callPostResetPassword(String mailToken, String newPassword) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", mailToken));
        urlParameters.add(new BasicNameValuePair("password", newPassword));
        return sendPOST(RESET_PASSWORD, urlParameters);
    }

    public static String callCheckUsername(String username) {
        URI uri = null;
        try {
            uri = new URIBuilder(CHECK_USERNAME + username).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    /**
     * @param uri   The URI where the GET request should be sent
     * @return      The server response
     */
    private static String sendGET(URI uri) {
        String result = null;
        try {
            final CloseableHttpClient httpClient = HttpClients.createDefault();
            final HttpGet request = new HttpGet(uri);
            final CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Send a POST request
     * @param url               The URL address where the POST request should be sent
     * @param urlParameters     The parameters of the POST request
     * @return                  The server response
     */
    private static String sendPOST(String url, List<NameValuePair> urlParameters) {
        String result = null;
        try {
            final HttpPost request = new HttpPost(url);
            request.setEntity(new UrlEncodedFormEntity(urlParameters));
            final CloseableHttpClient httpClient = HttpClients.createDefault();
            final CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
