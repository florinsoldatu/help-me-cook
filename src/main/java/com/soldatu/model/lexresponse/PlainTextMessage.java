package com.soldatu.model.lexresponse;

/**
 * Created by florin.soldatu on 21/04/2020.
 */

public class PlainTextMessage extends Message {

    private String content;

    public PlainTextMessage(String content) {
        this.contentType = "PlainText";
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
