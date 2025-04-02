package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Teacher {

    @Autowired
    @Qualifier("hpPrinter")
    private Printer printer;

    public void teach() {
        System.out.println("T'm a teacher.");
    }
}
