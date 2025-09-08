package com.example.dat250demo.pollapp.domains;

public class VoteOption {

    private int order;
    private String caption;

    public VoteOption() {}

    public VoteOption(int order, String caption) {
        this.order = order; this.caption = caption;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}