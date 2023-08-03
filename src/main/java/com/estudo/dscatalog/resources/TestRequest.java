package com.estudo.dscatalog.resources;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestRequest {

    @GetMapping
    public ResponseEntity<String> testString(){
        String test = "testando requisição";
        return ResponseEntity.ok().body(test);
    }
}
