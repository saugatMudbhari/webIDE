package com.share.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class UserOperationController {

    @PostMapping("/hello")
    public String hello(String id) {
        return "Hello, World!";
    }
}