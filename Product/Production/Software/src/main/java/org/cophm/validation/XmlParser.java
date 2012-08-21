package org.cophm.validation;

import org.apache.log4j.Logger;
import org.cophm.util.Constants;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by
 * User: ralph
 * Date: 4/18/12
 * Time: 9:32 PM
 */
public class XmlParser extends Parser implements IDataParser {

    private Document    doc;
    private Element     root;

    private Logger      log = Logger.getLogger(this.getClass().getName());


    public XmlParser() {
    }

    public void loadData(String data) throws IOException {
        SAXBuilder      builder = new SAXBuilder();
        String          xmlHeader;

        if(data.startsWith("<?")) {
            xmlHeader = "";
        }
        else {
            xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        }

        try {
            doc = builder.build(new StringReader(xmlHeader + data));

            root = doc.getRootElement();
        }
        catch(JDOMException e) {
            log.error("Caught a " + e.getClass().getName() + ": " + e.toString());
            log.error("XML Data = [" + data + "]");
            throw new IOException(e.getMessage());
        }
    }

    public String getFieldValue(String segmentName, String fieldNumber) {

        return getFieldValue(segmentName, null, fieldNumber);
    }

    public String getFieldValue(Element location) throws HL7ValidatorException {
        String            value;
        String            segmentName;
        String            fieldNumber;
        List<Element>     identifiers;

        segmentName = location.getChildText(Constants.HL7_SEGMENT, location.getNamespace()).trim();
        fieldNumber = location.getChild(Constants.HL7_SEGMENT,
                                        location.getNamespace()).getAttributeValue(Constants.FIELD_NUMBER,
                                                                                   location.getNamespace());
        identifiers = location.getChildren(Constants.IDENTIFIER, location.getNamespace());

        value = getFieldValue(segmentName, identifiers, fieldNumber);

        return value;
    }

    protected String getFieldValue(String segmentName, List<Element> identifiers, String fieldNumber) {
        Element     segment = null;
        Element     field;
        Element     subField;
        int         fieldNumberInt;
        String      fieldName;
        String      value;
        Iterator    identifierIterator;
        Iterator    segmentIterator;
        boolean     matchFound = false;

        if(identifiers == null || identifiers.size() == 0) {
            segment = root.getChild(segmentName, root.getNamespace());
        }
        else {
            //
            //  Implementation of identifiers.
            //
            matchFound = false;
            segmentIterator = root.getChildren(segmentName, root.getNamespace()).iterator();
             while(segmentIterator.hasNext()) {
                 Element       segmentToCheck = (Element)segmentIterator.next();

                 identifierIterator = identifiers.iterator();
                 while(identifierIterator.hasNext()) {
                     Element             identifier = (Element)identifierIterator.next();
                     String              matchValue;
                     boolean             mustMatch;
                     String              idFieldNumberStr;
                     RepeatingElement    repeatingElement;
                     String              valueToCompare;

                     matchValue = identifier.getText().trim();
                     idFieldNumberStr = identifier.getAttributeValue(Constants.FIELD_NUMBER,
                                                                     identifier.getNamespace());
                     mustMatch = identifier.getAttributeValue(Constants.MUST_MATCH,
                                                              identifier.getNamespace(), "true").equalsIgnoreCase("true");
                     repeatingElement = getRepeatingElement(identifier.getAttributeValue(Constants.REPEATING_ELEMENT,
                                                                                         identifier.getNamespace(), "Segment"));

                    if(repeatingElement.ordinal() == RepeatingElement.Segment.ordinal()) {
                        valueToCompare = getFieldValue(segmentToCheck, idFieldNumberStr);
                        if(valueToCompare.equals(matchValue)) {
                            segment = segmentToCheck;
                            matchFound = true;
                        }
                        else {
                            if(mustMatch) {
                                matchFound = false;
                                break;
                            }
                        }
                    }
                    else {
                        List<Element>     fields;
                        Iterator          fieldsIterator;

                        fields = getAllFields(segmentToCheck, idFieldNumberStr);
                        fieldsIterator = fields.iterator();
                        while(fieldsIterator.hasNext()) {
                            Element       fieldToCheck = (Element)fieldsIterator.next();

                            fieldName = fieldToCheck.getName() + "." + getSubFieldNumber(idFieldNumberStr);
                            subField = fieldToCheck.getChild(fieldName, fieldToCheck.getNamespace());
                            if(subField != null && subField.getText() != null
                                && subField.getText().trim().equals(matchValue)) {
                                int     subFieldNumber;

                                subFieldNumber = getSubFieldNumber(fieldNumber);
                                if(subFieldNumber > 0) {
                                    fieldName = fieldToCheck.getName() + "." + subFieldNumber;
                                    subField = fieldToCheck.getChild(fieldName, fieldToCheck.getNamespace());
                                }
                                return subField.getText().trim();
                            }
                        }
                    }
                }

                if(matchFound) {
                    break;
                }

             }

            if(matchFound == false) {
                return "";
            }
        }

        if(segment == null) {
            return "";
        }

        fieldNumberInt = getFieldNumber(fieldNumber);

        fieldName = segmentName + "." + fieldNumberInt;

        field = segment.getChild(fieldName, segment.getNamespace());

        if(field == null) {
            return "";
        }

        //
        // In order to maintain compatibility with the way HL7's Pipe Delimited rules,
        // we will look for a "*.1" child Element because in the Pipe Delimited format,
        // MSG|...|MSGID001|... is equal to MSG|...|MASGID001^|... but in XML format
        // it must be portrayed as <MSG.12.1>, even though it might be referenced as
        // MSG.12 (i.e. segment name = MSG and field number = 12).
        //
        if(getSubFieldNumber(fieldNumber) == -1) {
            value = field.getText().trim();
            if(value.length() == 0) {
                value = field.getChildText(fieldName + ".1", field.getNamespace());
                if(value == null) {
                    value = "";
                }
                return value.trim();
            }
        }

        fieldName = fieldName + "." + getSubFieldNumber(fieldNumber);

        subField = field.getChild(fieldName, field.getNamespace());

        if(subField != null) {
            return subField.getText().trim();
        }

        return "";
    }

    private List<Element> getAllFields(Element segment, String fieldNumberStr) {
        String                fieldName;
        List<Element>         fields;

        fieldName = segment.getName() + "." + getFieldNumber(fieldNumberStr);

        fields = segment.getChildren(fieldName, segment.getNamespace());

        return fields;
    }

    private String getFieldValue(Element segment, String filedNumberStr) {
        int           subFiledNumber;
        Element       fieldElement;
        Element       subFieldElement;
        String        fieldName;

        fieldName = segment.getName() + "." + getFieldNumber(filedNumberStr);

        fieldElement = segment.getChild(fieldName, segment.getNamespace());

        subFiledNumber = getSubFieldNumber(filedNumberStr);

        if(subFiledNumber == -1) {
            return fieldElement.getText().trim();
        }

        fieldName = fieldName + "." + subFiledNumber;

        subFieldElement = fieldElement.getChild(fieldName, fieldElement.getNamespace());

        if(subFieldElement == null) {
            if(subFiledNumber == 1) {
                return fieldElement.getText().trim();
            }
            else {
                return "";
            }
        }

        return subFieldElement.getText().trim();
    }
}
