package com.soldatu.model.lexresponse;

/**
 * Created by florin.soldatu on 21/04/2020.
 */
public class LexResponse {

    private DialogAction dialogAction;

    public LexResponse(DialogAction dialogAction) {
        this.dialogAction = dialogAction;
    }

    public DialogAction getDialogAction() {
        return dialogAction;
    }

    public void setDialogAction(DialogAction dialogAction) {
        this.dialogAction = dialogAction;
    }

}
