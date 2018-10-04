package com.sliit.ssd.csrfapp.services;

import com.sliit.ssd.csrfapp.models.Credentials;
import com.sliit.ssd.csrfapp.models.Session;
import com.sliit.ssd.csrfapp.models.CredentialsStore;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.UUID;

/**
 * Handles authentication related tasks
 *
 * Created by rkavushica on 9/5/18.
 */

@Service
public class LoginService {


    CredentialsStore userCredentialsStore = CredentialsStore.getCredentialsStore();

    /**
     * Authenticates user using username and password
     *
     * @param username
     * @param password
     * @return
     */
    public boolean isUserAuthenticated(String username, String password){
            return (username.equalsIgnoreCase("admin")
                    && password.equalsIgnoreCase("admin"));
    }

    /**
     * Authenticates user using cookies
     *
     * @param cookies
     * @return
     */
    public boolean isAuthenticated(Cookie[] cookies){
        String session = "";
        String username = "";

        if (null != cookies && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionID")){
                    session = cookie.getValue();
                } else if (cookie.getName().equals("username")){
                    username = cookie.getValue();
                }
            }
        }

        return (isUserSessionValid(username, session));
    }

    /**
     * Retrieves the session ID from cookies
     *
     * @param cookies
     * @return
     */
    public String sessionIdFromCookies(Cookie[] cookies){
        if (null != cookies && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionID")){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Checks if the sessionID is valid for the user
     *
     * @param username
     * @param sessionId
     * @return
     */
    public boolean isUserSessionValid(String username, String sessionId){
        if (CredentialsStore.getCredentialsStore().findCredentials(username) != null){
            return sessionId.equals(userCredentialsStore
                    .findCredentials(username)
                    .getSessionID());
        }
        return false;
    }

    /**
     * Generates a new session ID
     *
     * @param username
     * @return
     */
    public String generateSessionId(String username){

        String sessionId = UUID.randomUUID().toString();
        Credentials credentials = userCredentialsStore.findCredentials(username);
        credentials.setSessionID(sessionId);
        generateToken(sessionId);
        userCredentialsStore.saveCredentials(username, credentials);
        return sessionId;
    }

    /**
     * Generates a new anti-CSRF token
     *
     * @param session
     * @return
     */
    public String generateToken(String session){
        String token = session + System.currentTimeMillis();
        Session.getUserCredentialsStore().addSessionToken(session, token);
        return token;
    }

    /**
     * Validates if the CSRF token is valid
     *
     * @param sessionID
     * @param token
     * @return
     */
    public boolean validateCSRFToken(String sessionID, String token){
        if (null != token){
            return token.equals(Session.getUserCredentialsStore().getTokenFromSession(sessionID));
        }
        return false;
    }

}
