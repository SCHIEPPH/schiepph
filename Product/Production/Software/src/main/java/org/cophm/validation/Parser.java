package org.cophm.validation;

/**
 * Created by
 * User: ralph
 * Date: 4/18/12
 * Time: 10:12 PM
 */
public class Parser {

    protected int getFieldNumber(String fieldNumber) {
        int       number;
        int       dotIdx;

        dotIdx = fieldNumber.indexOf(".");
        if(dotIdx == -1) {
            dotIdx = fieldNumber.length();
        }
        number = Integer.parseInt(fieldNumber.substring(0,dotIdx));

        return number;
    }

    protected int getSubFieldNumber(String fieldNumber) {
        int       number;
        int       firstDotIdx;
        int       secondDotIdx;

        firstDotIdx = fieldNumber.indexOf(".");
        if(firstDotIdx == -1) {
            return -1;
        }
        else {
            secondDotIdx = fieldNumber.substring(firstDotIdx + 1).indexOf(".");
            if(secondDotIdx == -1) {
                secondDotIdx = fieldNumber.length();
            }
        }
        try {
            number = Integer.parseInt(fieldNumber.substring(firstDotIdx + 1, secondDotIdx));
        }
        catch(NumberFormatException e) {
            number = -1;
        }

        return number;
    }

    protected RepeatingElement getRepeatingElement(String repeatingElementName) {

        if(repeatingElementName.equalsIgnoreCase(RepeatingElement.Field.name())) {
            return RepeatingElement.Field;
        }
        else {
            return RepeatingElement.Segment;
        }
    }

}
