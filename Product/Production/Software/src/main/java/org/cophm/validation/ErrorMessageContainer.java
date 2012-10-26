package org.cophm.validation;

import org.cophm.util.XMLDefs;
import org.jdom.Element;

/**
 * Created by
 * User: ralph
 * Date: 4/25/12
 * Time: 2:32 PM
 */
public class ErrorMessageContainer {

    private int         id = -1;
    private boolean     printFieldName = true;
    private boolean     printFieldValue = false;
    private boolean     supressAdditionalMessage = false;
    private String      message = "";


    public ErrorMessageContainer(Element errorMessageElement) {

        if(errorMessageElement == null) {
            throw new IllegalArgumentException("Error message element may not be null.");
        }

        id = Integer.parseInt(errorMessageElement.getAttributeValue(XMLDefs.ID, errorMessageElement.getNamespace()));
        message = errorMessageElement.getText().trim();
        printFieldName = Boolean.valueOf(errorMessageElement.getAttributeValue(XMLDefs.PRINT_FIELD_NAME,
                                                                               errorMessageElement.getNamespace(),
                                                                               "false"));
        printFieldValue = Boolean.valueOf(errorMessageElement.getAttributeValue(XMLDefs.PRINT_FIELD_VALUE,
                                                                               errorMessageElement.getNamespace(),
                                                                               "false"));
        supressAdditionalMessage = Boolean.valueOf(errorMessageElement.getAttributeValue(XMLDefs.SUPRESS_ADDITIONAL_MESSAGE,
                                                                               errorMessageElement.getNamespace(),
                                                                               "false"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPrintFieldName() {
        return printFieldName;
    }

    public void setPrintFieldName(boolean printFieldName) {
        this.printFieldName = printFieldName;
    }

    public boolean isPrintFieldValue() {
        return printFieldValue;
    }

    public void setPrintFieldValue(boolean printFieldValue) {
        this.printFieldValue = printFieldValue;
    }

    public boolean isSupressAdditionalMessage() {
        return supressAdditionalMessage;
    }

    public void setSupressAdditionalMessage(boolean supressAdditionalMessage) {
        this.supressAdditionalMessage = supressAdditionalMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
