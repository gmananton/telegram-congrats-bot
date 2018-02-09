package com.gman.telegram.controller;

import com.gman.telegram.bot.CongratsBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

//    @Autowired
//    CongratsBot bot;
//
    @RequestMapping("index")
    public String index() {
        return "index";
    }
}
