package com.hoozad.pilot.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WidgetController {

    @RequestMapping("/widget.html")
    public String index() {
        return "widget";
    }

}
