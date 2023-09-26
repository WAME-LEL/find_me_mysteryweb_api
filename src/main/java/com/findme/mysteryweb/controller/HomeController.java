package com.findme.mysteryweb.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    @RequestMapping
    public String Home(){
        log.info("home page");

        return "home";
    }


}
