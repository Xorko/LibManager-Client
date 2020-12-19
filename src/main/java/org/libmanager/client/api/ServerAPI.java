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
import org.libmanager.client.util.ServerConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ServerAPI {

    private static String GET_ALL_BOOKS;
    private static String GET_ALL_DVD;
    private static String ADD_BOOK;
    private static String ADD_USER;
    private static String LOGIN;
    private static String FORGOT;
    private static String CHECK_USERNAME;

    static {
        try {
            String server = ServerConfig.getProperty("server.protocol") + "://" + ServerConfig.getProperty("server.host")
                    + ":" + ServerConfig.getProperty("server.port");

            GET_ALL_BOOKS   = server + "/item/get/books/all";
            GET_ALL_DVD     = server + "/item/get/dvd/all";
            ADD_BOOK        = server + "/item/book/add";
            ADD_USER        = server + "/user/add";
            LOGIN           = server + "/login";
            FORGOT          = server + "/user/forgot";
            CHECK_USERNAME  = server + "/user/checkusername";
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static String callLogin(String username, String password) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("password", password));
        return sendPOST(LOGIN, urlParameters);
    }

    public static String callCheckUsername(String username) {
        URI uri = null;
        try {
            uri = new URIBuilder(CHECK_USERNAME).addParameter("username", username).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    public static String callGetAllBooks() {
        URI uri = null;
        try {
            uri = new URIBuilder(GET_ALL_BOOKS).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return sendGET(uri);
    }

    public static void callGetForgot(String username) {
        //System.out.println(sendGET(FORGOT + "?token=" + username));
    }

    public static String callAddBook(String token, String isbn, String author, String title, String publisher, String publicationDate, String genre) {
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("token", token));
        urlParameters.add(new BasicNameValuePair("isbn", isbn));
        urlParameters.add(new BasicNameValuePair("author", author));
        urlParameters.add(new BasicNameValuePair("title", title));
        urlParameters.add(new BasicNameValuePair("publisher", publisher));
        urlParameters.add(new BasicNameValuePair("publicationDate", publicationDate));
        urlParameters.add(new BasicNameValuePair("genre", genre));
        return sendPOST(ADD_BOOK, urlParameters);
    }

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
