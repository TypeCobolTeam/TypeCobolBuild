package jtcb.util;

import java.util.Locale;
import java.util.ResourceBundle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Resources handling
 * @author MAYANJE
 */
public class Resources {
    /**
     * Messages bundle.
     */
    public static ResourceBundle bundle = ResourceBundle.getBundle("messages");
    /**
     * Get a message
     * @param key message's key
     * @param defaultMsg The defaulr message if there's no corresponding message
     * @return the message
     */
    public static String getMessage(String key, String defaultMsg)
    {
        String msg = bundle.getString(key);
        return msg == null ? key : msg;
    }

    /**
     * Get a message
     * @param key message's key
     * @return the message
     */
    public static String getMessage(String key)
    {
        return getMessage(key, key);
    }    
}
