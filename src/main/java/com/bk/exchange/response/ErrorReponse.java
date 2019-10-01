package com.bk.exchange.response;

public class ErrorReponse {

    private int status;

    private String title;

    private String description;

    public ErrorReponse(int status, String title, String description) {
        this.status = status;
        this.description = description;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
