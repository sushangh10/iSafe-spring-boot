package org.example.controller;
import org.springframework.web.bind.annotation.*;


/**
 * @Author sushanghai
 * @Date 2021/1/31
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

}

