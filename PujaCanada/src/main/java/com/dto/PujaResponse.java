package com.dto;

import com.Model.Puja;

public class PujaResponse {
    private Puja puja;
    private String imageBase64;

    // Constructors
    public PujaResponse(Puja puja, String imageBase64) {
        this.puja = puja;
        this.imageBase64 = imageBase64;
    }

    // Getters and setters
    public Puja getPuja() {
        return puja;
    }

    public void setPuja(Puja puja) {
        this.puja = puja;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
