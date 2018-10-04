package com.sliit.ssd.csrfapp.controllers;

import com.sliit.ssd.csrfapp.models.Credentials;
import com.sliit.ssd.csrfapp.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for handling login requests
 *
 * Created by rkavushica on 9/5/18.
 */

@Controller
public class AuthController {

    @Autowired
    LoginService authenticationService;

    @PostMapping("/login")
    public String login(@ModelAttribute Credentials credentials, HttpServletResponse response){
        String username = credentials.getUsername();
        if(authenticationService.isUserAuthenticated(username, credentials.getPassword())){
            Cookie sessionCookie = new Cookie("sessionID", authenticationService.generateSessionId(username));
            Cookie userCookie = new Cookie("username", username);
            response.addCookie(sessionCookie);
            response.addCookie(userCookie);
            return "redirect:/home";
        }

        return "redirect:/login?status=failed";
    }
}
