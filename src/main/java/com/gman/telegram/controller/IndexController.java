package com.gman.telegram.controller;

import com.gman.telegram.bot.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    @Autowired
    Bot bot;

    @RequestMapping("index")
    public String index() {
        return bot.getBotUsername() + " : " + bot.getBotToken();
    }
}
