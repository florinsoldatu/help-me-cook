package com.soldatu.model.lexresponse;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by florin.soldatu on 02/05/2020.
 */
public class ConfirmIntentDialogAction extends DialogAction {
    private String intentName;
    private HashMap<String,String> slots = new LinkedHashMap<>();

    public ConfirmIntentDialogAction(){
    }

    public ConfirmIntentDialogAction(String type, Message message){
        setType(type);
        setMessage(message);
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public HashMap<String, String> getSlots() {
        return slots;
    }

    public void addSlot(String key, String value){
        slots.put(key, value);
    }
}
