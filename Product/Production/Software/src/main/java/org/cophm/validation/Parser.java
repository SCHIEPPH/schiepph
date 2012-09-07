package org.cophm.validation;

import org.cophm.util.Constants;
import org.jdom.Element;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by
 * User: ralph
 * Date: 4/18/12
 * Time: 10:12 PM
 */
public class Parser {


    public static boolean inputIsXML(String data) {
        if(data.indexOf("<MSH.12") != -1 ||
            data.indexOf("<Msh.12") != -1 ||
            data.indexOf("<msh.12 ") != -1) {
            return true;
        }

        return false;
    }

    public static IDataParser   getParser(String  data) throws IOException {
        IDataParser     parser;

        if(inputIsXML(data)) {
            parser = new XmlParser();
        }
        else {
            parser = new PipeParser();
        }

        parser.loadData(data);

        return parser;
    }

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

//    public String getFieldValue(Element root, List locationList) throws HL7ValidatorException {
//        String            value;
//        String            segmentName;
//        String            fieldNumber;
//        Element location;
//        Iterator locationIterator;
//        List<Element>     identifiers;
//
//        locationIterator = locationList.iterator();
//
//        while(locationIterator.hasNext()) {
//            location = (Element)locationIterator.next();
//            segmentName = location.getChildText(Constants.HL7_SEGMENT, location.getNamespace()).trim();
//            fieldNumber = location.getChild(Constants.HL7_SEGMENT,
//                                            location.getNamespace()).getAttributeValue(Constants.FIELD_NUMBER,
//                                                                                       location.getNamespace());
//            identifiers = location.getChildren(Constants.IDENTIFIER, location.getNamespace());
//
//            value = getFieldValue(root, segmentName, identifiers, fieldNumber);
//            if(value != null && value.trim().length() > 0) {
//                return value;
//            }
//        }
//
//        return "";
//    }
//
//    public String getFieldValue(Element root, String segmentName, String fieldNumber) {
//
//        return getFieldValue(root, segmentName, null, fieldNumber);
//    }
//
//    protected String getFieldValue(Element root, String segmentName, List<Element> identifiers, String fieldNumber) {
//        Element     segment = null;
//        Element     field;
//        Element     subField;
//        int         fieldNumberInt;
//        String      fieldName;
//        String      value;
//        Iterator    identifierIterator;
//        Iterator    segmentIterator;
//        boolean     matchFound = false;
//
//        if(identifiers == null || identifiers.size() == 0) {
//            segment = root.getChild(segmentName, root.getNamespace());
//        }
//        else {
//            //
//            //  Implementation of identifiers.
//            //
//            matchFound = false;
//            segmentIterator = root.getChildren(segmentName, root.getNamespace()).iterator();
//             while(segmentIterator.hasNext()) {
//                 Element    segmentToCheck = (Element)segmentIterator.next();
//
//                 identifierIterator = identifiers.iterator();
//                 while(identifierIterator.hasNext()) {
//                     Element              identifier = (Element)identifierIterator.next();
//                     String               matchValue;
//                     boolean              mustMatch;
//                     String               idFieldNumberStr;
//                     RepeatingElement     repeatingElement;
//                     String               valueToCompare;
//
//                     matchValue = identifier.getText().trim();
//                     idFieldNumberStr = identifier.getAttributeValue(Constants.FIELD_NUMBER,
//                                                                     identifier.getNamespace());
//                     mustMatch = identifier.getAttributeValue(Constants.MUST_MATCH,
//                                                              identifier.getNamespace(), "true").equalsIgnoreCase("true");
//                     repeatingElement = getRepeatingElement(identifier.getAttributeValue(Constants.REPEATING_ELEMENT,
//                                                                                         identifier.getNamespace(), "Segment"));
//
//                    if(repeatingElement.ordinal() == RepeatingElement.Segment.ordinal()) {
//                        valueToCompare = getFieldValue(segmentToCheck, idFieldNumberStr);
//                        if(valueToCompare.equals(matchValue)) {
//                            segment = segmentToCheck;
//                            matchFound = true;
//                        }
//                        else {
//                            if(mustMatch) {
//                                matchFound = false;
//                                break;
//                            }
//                        }
//                    }
//                    else {
//                        List<Element>     fields;
//                        Iterator          fieldsIterator;
//
//                        fields = getAllFields(segmentToCheck, idFieldNumberStr);
//                        fieldsIterator = fields.iterator();
//                        while(fieldsIterator.hasNext()) {
//                            Element       fieldToCheck = (Element)fieldsIterator.next();
//
//                            fieldName = fieldToCheck.getName() + "." + getSubFieldNumber(idFieldNumberStr);
//                            subField = fieldToCheck.getChild(fieldName, fieldToCheck.getNamespace());
//                            if(subField != null && subField.getText() != null
//                                && subField.getText().trim().equals(matchValue)) {
//                                int       subFieldNumber;
//
//                                subFieldNumber = getSubFieldNumber(fieldNumber);
//                                if(subFieldNumber > 0) {
//                                    fieldName = fieldToCheck.getName() + "." + subFieldNumber;
//                                    subField = fieldToCheck.getChild(fieldName, fieldToCheck.getNamespace());
//                                }
//                                return subField.getText().trim();
//                            }
//                        }
//                    }
//                }
//
//                if(matchFound) {
//                    break;
//                }
//
//             }
//
//            if(matchFound == false) {
//                return "";
//            }
//        }
//
//        if(segment == null) {
//            return "";
//        }
//
//        fieldNumberInt = getFieldNumber(fieldNumber);
//
//        fieldName = segmentName + "." + fieldNumberInt;
//
//        field = segment.getChild(fieldName, segment.getNamespace());
//
//        if(field == null) {
//            return "";
//        }
//
//        //
//        // In order to maintain compatibility with the way HL7's Pipe Delimited rules,
//        // we will look for a "*.1" child Element because in the Pipe Delimited format,
//        // MSG|...|MSGID001|... is equal to MSG|...|MASGID001^|... but in XML format
//        // it must be portrayed as <MSG.12.1>, even though it might be referenced as
//        // MSG.12 (i.e. segment name = MSG and field number = 12).
//        //
//        if(getSubFieldNumber(fieldNumber) == -1) {
//            value = field.getText().trim();
//            if(value.length() == 0) {
//                value = field.getChildText(fieldName + ".1", field.getNamespace());
//                if(value == null) {
//                    value = "";
//                }
//                return value.trim();
//            }
//        }
//
//        fieldName = fieldName + "." + getSubFieldNumber(fieldNumber);
//
//        subField = field.getChild(fieldName, field.getNamespace());
//
//        if(subField != null) {
//            return subField.getText().trim();
//        }
//
//        return "";
//    }
//
//    private List<Element> getAllFields(Element segment, String fieldNumberStr) {
//        String            fieldName;
//        List<Element>     fields;
//
//        fieldName = segment.getName() + "." + getFieldNumber(fieldNumberStr);
//
//        fields = segment.getChildren(fieldName, segment.getNamespace());
//
//        return fields;
//    }
//
//    private String getFieldValue(Element segment, String filedNumberStr) {
//        int           subFiledNumber;
//        Element       fieldElement;
//        Element       subFieldElement;
//        String        fieldName;
//
//        fieldName = segment.getName() + "." + getFieldNumber(filedNumberStr);
//
//        fieldElement = segment.getChild(fieldName, segment.getNamespace());
//
//        subFiledNumber = getSubFieldNumber(filedNumberStr);
//
//        if(subFiledNumber == -1) {
//            return fieldElement.getText().trim();
//        }
//
//        fieldName = fieldName + "." + subFiledNumber;
//
//        subFieldElement = fieldElement.getChild(fieldName, fieldElement.getNamespace());
//
//        if(subFieldElement == null) {
//            if(subFiledNumber == 1) {
//                return fieldElement.getText().trim();
//            }
//            else {
//                return "";
//            }
//        }
//
//        return subFieldElement.getText().trim();
//    }
}
