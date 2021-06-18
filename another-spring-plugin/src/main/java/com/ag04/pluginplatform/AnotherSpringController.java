package com.ag04.pluginplatform;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AnotherSpringController {

    @GetMapping("anotherSpringController")
    @ResponseBody
    public String get() {
        return "Hello from AnotherSpringController!";
    }
}
