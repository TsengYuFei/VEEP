package com.example.api;

import java.util.List;

public class Store {
    public List<String > productList;

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }
}
