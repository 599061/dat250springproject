package com.example.dat250demo.pollapp;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GuiController {

    @GetMapping("/gui")
    public ResponseEntity<String> showIndex() {
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body("""
                <html>
                <head>
                <title>GUI</title>
                </head>
                <body>
                <h1>Hello World!</h1>
                </body>
                </html>
                """);
    }
}
