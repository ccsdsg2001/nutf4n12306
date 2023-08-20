package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cc
 * @date 2023年08月20日 18:28
 */
@RestController
public class testcontroller {
    @GetMapping("/HELLO")
    public String test(){
        return "heelo";
    }
}
