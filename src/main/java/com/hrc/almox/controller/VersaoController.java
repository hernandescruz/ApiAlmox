package com.hrc.almox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/versao")
public class VersaoController {

    @GetMapping
    public String versao() {
        return "build-" + System.currentTimeMillis();
    }

}

