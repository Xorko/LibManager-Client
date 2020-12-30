package org.libmanager.client.util;

import org.libmanager.client.App;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.Preferences;

public class Config {

    private static final Preferences prefs = Preferences.userNodeForPackage(App.class);
    private static final String defaultConfigFile = "defaultconfig.properties";

    static {
        if (getServerProtocol() == null || getServerPort().length() == 0 ||getServerAddress() == null ||
                getServerPort().length() == 0 ||getServerPort() == null || getServerPort().length() == 0)
            reloadConfig();
    }

    /**
     * Get the server protocol from the preferences
     * @return The server protocol
     */
    public static String getServerProtocol() {
        return prefs.get("server_protocol", null);
    }

    /**
     * Set the server protocol in the preferences
     * @param protocol The server protocol
     */
    public static void setServerProtocol(String protocol) {
        prefs.put("server_protocol", protocol);
    }

    /**
     * Get the server address from the preferences
     * @return The server address
     */
    public static String getServerAddress() {
        return prefs.get("server_address", null);
    }

    /**
     * Set the server address in the preferences
     * @param address The server address
     */
    public static void setServerAddress(String address) {
        prefs.put("server_address", address);
    }

    /**
     * Get the server port from the preferences
     * @return The server port
     */
    public static String getServerPort() {
        return prefs.get("server_port", null);
    }

    /**
     * Set the server port in the preferences
     * @param port The server port
     */
    public static void setServerPort(String port) {
        prefs.put("server_port", port);
    }

    public static void reloadConfig() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultConfigFile);
        Properties props = new Properties();
        try {
            props.load(is);
            setServerProtocol(props.getProperty("server.protocol"));
            setServerAddress(props.getProperty("server.address"));
            setServerPort(props.getProperty("server.port"));
        } catch (IOException e) {
            System.out.println("Unable to load properties file " + defaultConfigFile);
        }
    }
}
