package com.clearview.docusign.hackathon.Agreement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

    @GetMapping("/callback")
    public String handleCallback(@RequestParam(required = false) String code,
                                 @RequestParam(required = false) String state,
                                 @RequestParam(required = false) String error) {
        if (error != null) {
            return "Error: " + error;
        }
        if (code != null) {

            return "Authorization code received: " + code;
        }
        return "No authorization code received.";
    }
}

