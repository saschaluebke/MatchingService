package web.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
public class APIController {

    @RequestMapping("/")
    public String start(ModelMap modelMap) {
        /*
        Initialization for Home - page
         */

        return "start";
    }
/*
    @RequestMapping(value = "/translated", method = RequestMethod.POST)
    public String recoverPass(@RequestParam Map<String, String> params, @RequestParam("word") String word, @RequestParam("to") String to, @RequestParam("from") String from,
                              @RequestParam("request") String request, @RequestParam("translation") String translate, @RequestParam("descPut") String descPut,
                              @RequestParam("wordPut") String wordPut, @RequestParam("fromPut") String fromPut, @RequestParam("prior") int prior,
                              @RequestParam("newDescPut") String newDescPut, final ModelMap modelMap) {

        return "start";
    }
    */
}