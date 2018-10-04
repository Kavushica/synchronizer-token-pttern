package com.sliit.ssd.csrfapp.controllers;

import com.sliit.ssd.csrfapp.models.Fund;
import com.sliit.ssd.csrfapp.services.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles fund transfer functions
 *
 * Created by rkavushica on 9/6/18.
 */
@Controller
public class FundTransferController {

    @Autowired
    LoginService authenticationService;

    @PostMapping("/transfer")
    public String transferFunds(@ModelAttribute Fund fundTransfer, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String sessionId = authenticationService.sessionIdFromCookies(cookies);
        if (authenticationService.isAuthenticated(cookies) && authenticationService.validateCSRFToken(sessionId, fundTransfer.getCsrf())){
            authenticationService.generateToken(sessionId);
            return "redirect:/home?status=success";
        }
        return "redirect:/home?status=failed";
    }


}
