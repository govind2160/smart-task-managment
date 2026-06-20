package com.example.smarttask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {
    
    @RequestMapping(value = {
        "/",
        "/login",
        "/register",
        "/dashboard",
        "/projects",
        "/tasks",
        "/users"
    })
    public String forward() {
        return "forward:/index.html";
    }
}
