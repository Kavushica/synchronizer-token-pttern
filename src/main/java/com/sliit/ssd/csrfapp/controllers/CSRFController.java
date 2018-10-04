package com.sliit.ssd.csrfapp.controllers;

import com.sliit.ssd.csrfapp.exceptions.UnauthorizedException;
import com.sliit.ssd.csrfapp.models.Session;
import com.sliit.ssd.csrfapp.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rkavushica on 9/6/18.
 */

@RestController
public class CSRFController {


    @Autowired
    LoginService authenticationService;

    @GetMapping(path="/token")
    public String getToken(HttpServletRequest request) throws UnauthorizedException {
        String sessionId = authenticationService.sessionIdFromCookies(request.getCookies());
        if (null != sessionId && null != Session.getUserCredentialsStore().getTokenFromSession(sessionId)){
            return Session.getUserCredentialsStore().getTokenFromSession(sessionId);
        }
        throw new UnauthorizedException();
    }
}
