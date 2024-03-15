package com.example.projedeneme;

import androidx.annotation.NonNull;

public class Product {

    private String name;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private String link;
    private String price;
    private String source;

    public Product(String name, String imageUrl, String link, String price, String source) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.link = link;
        this.price = price;
        this.source = source;
    }

    void setName(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void setPrice(String price) {
        this.price = price;
    }

    String getPrice() {
        return price;
    }

    void setSource(String source) {
        this.source = source;
    }

    String getSource() {
        return source;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}