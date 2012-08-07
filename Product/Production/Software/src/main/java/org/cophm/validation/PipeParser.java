package org.cophm.validation;

//import ca.uhn.hl7v2.HL7Exception;
import org.cophm.util.Constants;
import org.jdom.Element;
import sun.util.resources.LocaleNames_el;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;

/**
 * Created by
 * User: ralph
 * Date: 4/18/12
 * Time: 8:53 PM
 */
public class PipeParser extends Parser implements IDataParser {

    public HashMap<String, ArrayList>     hl7DataMap;

    private String      subFieldSeparator;
    private String      repeatingFieldSeparator;

    public static String fieldSeparator = "|";


    public PipeParser() {
    }

    public String  getFieldValue(Element  location)
            throws HL7ValidatorException {
        String            value;
        String            segmentName;
        String            fieldNumber;
        List<Element>     identifiers;

        segmentName = location.getChildText(Constants.HL7_SEGMENT, location.getNamespace());
        fieldNumber = location.getChild(Constants.HL7_SEGMENT,
                                        location.getNamespace()).getAttributeValue(Constants.FIELD_NUMBER,
                                                                                   location.getNamespace());
        identifiers = location.getChildren(Constants.IDENTIFIER, location.getNamespace());

        value = getFieldValue(segmentName, identifiers, fieldNumber);

        return value;
    }

    public void loadData(String  pipeDelimitedData) throws IOException {
        LineNumberReader      reader = new LineNumberReader(new CharArrayReader(pipeDelimitedData.toCharArray()));
        String                segmentName;
        ArrayList<String>     segmentList;
        String                line;

        hl7DataMap = new HashMap<String, ArrayList>();

        while((line = reader.readLine()) != null) {
            StringTokenizer       strtok;

            if(line.trim().length() == 0) {
                continue;
            }

            //
            // The first character following the MSH in the MSH record is overloaded and
            // is both a field separator and a the definition of the field separator ,
            // making it a filed as well as a separator (yuck, yuck, yuck).
            // Because of this, we will add one more field separator to the MSH record
            // so it's structure remains consistent with the other records.
            //
            if(line.startsWith("MSH")) {
                fieldSeparator = line.substring(3,4);
                line = "MSH" + fieldSeparator + line.substring(3);
            }

            strtok = new StringTokenizer(line, fieldSeparator);
            segmentName = strtok.nextToken().toUpperCase();
            segmentList = hl7DataMap.get(segmentName);
            if(segmentList == null) {
                segmentList = new ArrayList<String>();
                hl7DataMap.put(segmentName, segmentList);
            }
            segmentList.add(line);
        }

        subFieldSeparator = String.valueOf(getFieldValue("MSH", 0, "2").charAt(0));
        repeatingFieldSeparator = String.valueOf(getFieldValue("MSH", 0, "2").charAt(1));
    }

    public String  getFieldValue(String  segmentName, String fieldNumber) {

        return getFieldValue(segmentName, 0, fieldNumber);
    }

    protected int  getOccuranceCount(String segmentName) {

        return hl7DataMap.get(segmentName).size();
    }

    protected String  getFieldValue(String  segmentName, List<Element> identifiers, String fieldNumber) {
        String                                    idFieldNumber;
        boolean                                   mustMatch;
        boolean                                   matchFound;
        String                                    matchValue;
        String                                    valueToCheck = "";
        String                                    valueToReturn = "";
        Iterator                                  identifierIterator;
        int                                       segmentCount;
        int                                       currentSegement = 0;
        RepeatingElement     repeatingElement;


        if(identifiers == null || identifiers.size() == 0) {
            return getFieldValue(segmentName, 0, fieldNumber);
        }

        segmentCount = getOccuranceCount(segmentName);

        matchFound = false;

        identifierIterator = identifiers.iterator();
        while(identifierIterator.hasNext()) {
            Element       identifier = (Element)identifierIterator.next();

            idFieldNumber = identifier.getAttributeValue(Constants.FIELD_NUMBER,
                                                                          identifier.getNamespace(), "0");
            mustMatch = Boolean.parseBoolean(identifier.getAttributeValue(Constants.MUST_MATCH,
                                                                          identifier.getNamespace(), "true"));
            matchValue = identifier.getTextTrim();

            repeatingElement = getRepeatingElement(identifier.getAttributeValue(Constants.REPEATING_ELEMENT,
                                                                          identifier.getNamespace(), "Segment"));

            matchFound = false;
            for(currentSegement = 0; currentSegement < segmentCount; currentSegement++) {
                if(repeatingElement.ordinal() == RepeatingElement.Segment.ordinal()) {
                    valueToCheck = getFieldValue(segmentName, currentSegement, idFieldNumber);
                    if(matchValue.equals(valueToCheck)) {
                        valueToReturn = getFieldValue(segmentName, currentSegement, fieldNumber);
                        matchFound = true;
                        break;
                    }
                }
                else {
                    ArrayList<String>     values;
                    Iterator              valuesIterator;

                    values = getAllFieldValues(segmentName, currentSegement, idFieldNumber);
                    valuesIterator = values.iterator();
                    while(valuesIterator.hasNext()) {
                        String      fieldValue = (String)valuesIterator.next();

                        valueToCheck = getSubFieldValue(fieldValue, idFieldNumber);
                        if(valueToCheck.equals(matchValue)) {
                            valueToReturn = getSubFieldValue(fieldValue, fieldNumber);
                            matchFound = true;
                            break;
                        }
                    }
                }
            }

            if(mustMatch ==  true) {
                if(matchFound == false) {
                    return "";
                }
                else {
                    return valueToReturn;
                }
            }
            if(matchFound) {
                return valueToReturn;
            }
        }

        return "";
    }

    protected ArrayList<String>  getAllFieldValues(String segmentName, int currentSegement, String fieldNumber) {
        ArrayList<String>     values = new ArrayList<String>();
        StringTokenizer       strtok;

        strtok = new StringTokenizer(getFieldValue(segmentName, currentSegement, String.valueOf(getFieldNumber(fieldNumber))),
                                     repeatingFieldSeparator,     true);

        while(strtok.hasMoreTokens()) {
            String      data;

            data = strtok.nextToken();
            if(data.equals(repeatingFieldSeparator)) {
                data = "";
            }
            else {
                //
                // Eat the separator.
                //
                if(strtok.hasMoreTokens()) {
                    strtok.nextToken();
                }
            }
            values.add(data);
        }

        return values;
    }


    protected String  getFieldValue(String segmentName, int occurance, String fieldNumber) {
        StringTokenizer       strtok;
        String                segment;
        int                   fieldNumberInt;
        String                value;
        ArrayList             listOfSegments;

        listOfSegments = hl7DataMap.get(segmentName);
        if(listOfSegments == null) {
            return "";
        }

        segment = (String)listOfSegments.get(occurance);
        if(segment == null) {
            return "";
        }

        fieldNumberInt = getFieldNumber(fieldNumber);
        strtok = new StringTokenizer(segment, fieldSeparator, true);
        //
        // Eat the segment name and the following seperator.
        //
        strtok.nextToken();
        strtok.nextToken();

        while(fieldNumberInt > 1 && strtok.hasMoreTokens()) {
            value = strtok.nextToken();
            if(value.equals(fieldSeparator) == false) {
                if(strtok.hasMoreTokens()) {
                    strtok.nextToken();
                }
            }
            fieldNumberInt--;
        }

        if(strtok.hasMoreTokens()) {
            value = strtok.nextToken();
            if(value.equals(fieldSeparator) == true) {
                value = "";
            }
            if(getSubFieldNumber(fieldNumber) > -1) {
                value = getSubFieldValue(value, fieldNumber);
            }
        }
        else {
            value = "";
        }

        return value;
    }

    protected String  getSubFieldValue(String  field, String fieldNumber) {
        StringTokenizer       strtok;
        int                   subFieldNumber;
        String                value;

        subFieldNumber = getSubFieldNumber(fieldNumber);
        strtok = new StringTokenizer(field, subFieldSeparator, true);
            while(subFieldNumber > 1 && strtok.hasMoreTokens()) {
                value = strtok.nextToken();
                if(value.equals(subFieldSeparator) == false) {
                    if(strtok.hasMoreTokens()) {
                        strtok.nextToken();
                    }
                }
                subFieldNumber--;
            }

        if(strtok.hasMoreTokens()) {
            value = strtok.nextToken();
            if(value.equals(subFieldSeparator) == true) {
                value = "";
            }
        }
        else {
            value = "";
        }

        return value;
    }

    protected String getSubFieldSeparator() {
        return subFieldSeparator;
    }

    protected String getRepeatingFieldSeparator() {
        return repeatingFieldSeparator;
    }

}
