package com.example.api;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CanonPrinter implements Printer {

//    @Value( "${printer.count}")
//    private int count;  // 預設=0

    @Override
    public void print(String message) {
//        count--;
        System.out.println("Canon Printer: " + message);
//        System.out.println("Left: " + count + "times");
    }
}
