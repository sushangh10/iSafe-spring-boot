package org.example.controller;

import org.example.beans.OfficialREST;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author sushanghai
 * @Date 2021/2/3
 */

@RestController
public class OfficialRESTController {
    @GetMapping("/official")
    public OfficialREST official() {
        Map value = new HashMap();
        value.put("id", 10);
        value.put("quote", "Hello, official restful api");
        OfficialREST officialREST = new OfficialREST();
        officialREST.setType("success");
        officialREST.setValue(value);
        return officialREST;
    }
}
