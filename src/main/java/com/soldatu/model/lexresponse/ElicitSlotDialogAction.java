package com.soldatu.model.lexresponse;

/**
 * Created by florin.soldatu on 01/05/2020.
 */
public class ElicitSlotDialogAction extends ConfirmIntentDialogAction {

    private String slotToElicit;

    public String getSlotToElicit() {
        return slotToElicit;
    }

    public void setSlotToElicit(String slotToElicit) {
        this.slotToElicit = slotToElicit;
    }
}
