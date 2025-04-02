package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Hello {
    @Autowired
    @Qualifier("canonPrinter")
    private Printer printer;

    @RequestMapping("/test")
    public String test() {
        printer.print("Hi!");
        return "Hello World";
    }

    @RequestMapping("/product")
    public Store product() {
        Store store = new Store();
        List<String> list = new ArrayList<>();
        list.add("apple");
        list.add("orange");
        list.add("banana");
        store.setProductList(list);
        return store;
    }
}