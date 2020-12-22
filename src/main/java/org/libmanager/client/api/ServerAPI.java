package org.libmanager.client.api;

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

public class ServerAPI {

    private static String GET_ALL_BOOKS;
    private static String GET_ALL_DVD;
    private static String SEARCH_BOOK;
    private static String SEARCH_DVD;
    private static String ADD_BOOK;
    private static String ADD_DVD;
    private static String ADD_USER;
    private static String EDIT_BOOK;
    private static String EDIT_DVD;
    private static String DELETE_ITEM;
    private static String LOGIN;
    private static String FORGOT;
    private static String CHECK_USERNAME;

    static {
        // Initialize all of the endpoints
        try {
            String server = Config.getProperty("server.protocol") + "://" + Config.getProperty("server.host")
                    + ":" + Config.getProperty("server.port");

            GET_ALL_BOOKS   = server + "/item/book/get/all";
            GET_ALL_DVD     = server + "/item/dvd/get/all";
            SEARCH_BOOK     = server + "/item/book/search";
            SEARCH_DVD      = server + "/item/dvd/search";
            ADD_BOOK        = server + "/item/book/add";
            ADD_DVD         = server + "/item/dvd/add";
            ADD_USER        = server + "/user/add";
            EDIT_BOOK       = server + "/item/book/edit";
            EDIT_DVD        = server + "/item/dvd/edit";
            DELETE_ITEM     = server + "/item/delete";
            LOGIN           = server + "/login";
            FORGOT          = server + "/user/forgot";
            CHECK_USERNAME  = server + "/user/checkusername";
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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
    public static String callGetAllDVD() {
        URI uri = null;
        try {
            uri = new URIBuilder(GET_ALL_DVD).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
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
     * Call the ADD_BOOK endpoint
     * @param token         The token of the logged in user (must be admin)
     * @param isbn          The isbn of the book to add
     * @param author        The author of the book to add
     * @param title         The title of the book to add
     * @param publisher     The publisher of the book to add
     * @param releaseDate   The release date of the book to add
     * @param genre         The genre of the book to add
     * @param copies        The number of copies of the book
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
     * @param director      The director of the movie
     * @param title         The title of the movie
     * @param duration      The duration of the movie
     * @param releaseDate   The release date of the movie
     * @param genre         The genre of the dvd
     * @param totalCopies   The number of copies of the DVD
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
     * Call the EDIT_BOOK endpoint
     * @param token         The token of the user (must be admin)
     * @param id            The id of the book to edit
     * @param isbn          The isbn of the book
     * @param author        The author of the book
     * @param title         The title of the book
     * @param publisher     The publisher of the book
     * @param releaseDate   The release date of the book
     * @param genre         The genre of the book
     * @param totalCopies        The number of copies of the DVD
     * @return              The server response
     */
    public static String callEditBook(String token, int id, String isbn, String author, String title, String publisher,
                                      String releaseDate, String genre, int totalCopies) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("id", Integer.toString(id)));
        urlParameters.add(new BasicNameValuePair("isbn", isbn));
        urlParameters.add(new BasicNameValuePair("author", author));
        urlParameters.add(new BasicNameValuePair("title", title));
        urlParameters.add(new BasicNameValuePair("publisher", publisher));
        urlParameters.add(new BasicNameValuePair("releaseDate", releaseDate));
        urlParameters.add(new BasicNameValuePair("genre", genre));
        urlParameters.add(new BasicNameValuePair("totalCopies", Integer.toString(totalCopies)));
        return sendPOST(EDIT_BOOK, urlParameters);
    }

    /**
     * Call the EDIT_DVD endpoint
     @param token         The token of the user (must be admin)
      * @param id            The id of the dvd to edit
     * @param author        The director of the movie
     * @param title         The title of the movie
     * @param duration      The duration of the movie
     * @param releaseDate   The release date of the movie
     * @param genre         The genre of the movie
     * @param totalCopies   The number of copies of the DVD
     * @return              The server response
     */
    public static String callEditDVD(String token, int id, String author, String title, String duration,
                                     String releaseDate, String genre, int totalCopies) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("id", Integer.toString(id)));
        urlParameters.add(new BasicNameValuePair("director", author));
        urlParameters.add(new BasicNameValuePair("title", title));
        urlParameters.add(new BasicNameValuePair("duration", duration));
        urlParameters.add(new BasicNameValuePair("releaseDate", releaseDate));
        urlParameters.add(new BasicNameValuePair("genre", genre));
        urlParameters.add(new BasicNameValuePair("totalCopies", Integer.toString(totalCopies)));
        return sendPOST(EDIT_DVD, urlParameters);
    }

    /**
     * Call the DELETE_ITEM endpoint
     * @param token The token of the user (must be admin)
     * @param id    The id of the item to delete
     * @return      The server response
     */
    public static String callDeleteItem(String token, int id) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("id", Integer.toString(id)));
        return sendPOST(DELETE_ITEM, urlParameters);
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
     * Call the CHECK_USERNAME endpoint
     * @param username  The username to check
     * @return          The server response
     */
    public static String callCheckUsername(String username) {
        URI uri = null;
        try {
            uri = new URIBuilder(CHECK_USERNAME).addParameter("username", username).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    /**
     * Call the FORGOT endpoint with GET method
     * @param username  The user who asks for a password reset
     * @return          The response of the server
     */
    public static String callGetForgot(String username) {
        URI uri = null;
        try {
            uri = new URIBuilder(FORGOT).addParameter("username", username).build();
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
