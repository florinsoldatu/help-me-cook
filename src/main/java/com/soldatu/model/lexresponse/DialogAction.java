package com.soldatu.model.lexresponse;

/**
 * Created by florin.soldatu on 21/04/2020.
 */

public class DialogAction {
    private String type;
    private String fulfillmentState;
    private Message message;

    public DialogAction() {
    }

    public DialogAction(String type, Message message){
        this.type = type;
        this.message = message;
    }

    public DialogAction(String type, String fulfillmentState, Message message) {
        this.type = type;
        this.fulfillmentState = fulfillmentState;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFulfillmentState() {
        return fulfillmentState;
    }

    public void setFulfillmentState(String fulfillmentState) {
        this.fulfillmentState = fulfillmentState;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}