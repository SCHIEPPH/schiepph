package org.cophm.validation;

import org.cophm.util.XMLDefs;
import org.jdom.Element;

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

    public static String    fieldSeparator = "|";


    public PipeParser() {
    }

    public String getFieldValue(List locationList) throws HL7ValidatorException {
        String      value;
        String      segmentName;
        String      fieldNumber;
        Element     location;
        Iterator    locationIterator;

        locationIterator = locationList.iterator();

        while(locationIterator.hasNext()) {
            location = (Element)locationIterator.next();
            segmentName = location.getChildText(XMLDefs.HL7_SEGMENT, location.getNamespace()).trim();
            fieldNumber = location.getChild(XMLDefs.HL7_SEGMENT,
                                            location.getNamespace()).getAttributeValue(XMLDefs.FIELD_NUMBER,
                                                                                       location.getNamespace());

            value = getFieldValue(segmentName, location, fieldNumber);
            if(value != null && value.trim().length() > 0) {
                return value;
            }
        }

        return "";
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

        subFieldSeparator = String.valueOf(getFieldValue("MSH", "2").charAt(0));
        repeatingFieldSeparator = String.valueOf(getFieldValue("MSH", "2").charAt(1));
    }

    public boolean  isSegmentPresent(String  segmentName) {
        ArrayList     segmentlist;

        segmentlist = hl7DataMap.get(segmentName);

        if(segmentlist == null || segmentlist.size() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public String  getFieldValue(String segmentName, String fieldNumber) {
        List<Integer>     segmentIndexList = new ArrayList<Integer>();

        segmentIndexList.add(new Integer(0));
        return getFieldValue(segmentName, segmentIndexList, fieldNumber);
    }

    protected int  getOccuranceCount(String segmentName) {

        return hl7DataMap.get(segmentName).size();
    }

    public List<String>  getAllFieldValues(List  locations) {
        Iterator              locationIterator;
        Element               hl7SegmentElement;
        ArrayList<String>     allValues = new ArrayList<String>();

        locationIterator = locations.iterator();
        while(locationIterator.hasNext()) {
            Element       locationElement = (Element)locationIterator.next();

            hl7SegmentElement = locationElement.getChild(XMLDefs.HL7_SEGMENT, locationElement.getNamespace());

            getFieldValue(hl7SegmentElement.getText(),
                          locationElement,
                          hl7SegmentElement.getAttributeValue(XMLDefs.FIELD_NUMBER, hl7SegmentElement.getNamespace()),
                          allValues);
        }

        return  allValues;
    }

    protected String  getFieldValue(String segmentName, Element  location, String fieldNumber) {

        return getFieldValue(segmentName, location, fieldNumber, null);
    }

    protected String  getFieldValue(String segmentName, Element  location,
                                    String fieldNumber, ArrayList<String>  allValues) {
        String            idFieldNumber;
        boolean           matchFound;
        String            matchValue;
        String            valueToCheck = "";
        String            valueToReturn = "";
        List<Element>     identifiers;
        Iterator          fieldValueListIterator;
        Iterator          identifierIterator;
        int               segmentCount;
        int               currentSegement = 0;
        List<Integer>     segmentIndexList;
        boolean           multipleValuesAllowed;
        boolean           repeatingSegment;

        identifiers = location.getChildren(XMLDefs.IDENTIFIER, location.getNamespace());

        multipleValuesAllowed = Boolean.parseBoolean(((Element)(location.getParent()))
                                                             .getAttributeValue(XMLDefs.CAN_CONTAIN_MULTIPLE_VALUES,
                                                                                location.getNamespace(), "false"));

        repeatingSegment = ((Element)(location.getParent()))
                .getAttributeValue(XMLDefs.REPEATING_ELEMENT,
                                   location.getNamespace(),
                                   RepeatingElement.Field.name()).equalsIgnoreCase(RepeatingElement.Segment.name());

        segmentIndexList = getSegmentIndexes(segmentName, identifiers);

        if(identifiers == null || identifiers.size() == 0) {
            if(multipleValuesAllowed && allValues != null) {
                allValues.addAll(getAllFieldValues(segmentName, segmentIndexList, fieldNumber));
            }
            else {
                segmentIndexList.clear();
                segmentIndexList.add(new Integer(0));
                valueToReturn =  getFieldValue(segmentName, segmentIndexList, fieldNumber);
                if(allValues != null) {
                    allValues.add(valueToReturn);
                }
            }

            return valueToReturn;
        }

        segmentCount = getOccuranceCount(segmentName);

        matchFound = false;

        identifierIterator = identifiers.iterator();
        while(identifierIterator.hasNext()) {
            Element       identifier = (Element)identifierIterator.next();

            idFieldNumber = identifier.getAttributeValue(XMLDefs.FIELD_NUMBER, identifier.getNamespace(), "0");
            matchValue = identifier.getTextTrim();

            matchFound = false;
            for(currentSegement = 0; currentSegement < segmentCount; currentSegement++) {
                if(multipleValuesAllowed) {
                    fieldValueListIterator = getAllFieldAndSubFieldValues(segmentName, currentSegement, fieldNumber).iterator();
                    while(fieldValueListIterator.hasNext()) {
                        String      fieldValue = (String)fieldValueListIterator.next();

                        if(repeatingSegment) {
                            List<Integer>       singleIndexList;

                            singleIndexList = new ArrayList<Integer>();
                            singleIndexList.add(new Integer(currentSegement));
                            valueToCheck = getFieldValue(segmentName, singleIndexList, idFieldNumber);
                        }
                        else {
                            valueToCheck = getSubFieldValue(fieldValue, idFieldNumber);
                        }
                        if(matchValue.equals(valueToCheck)) {
                            valueToReturn = getSubFieldValue(fieldValue, fieldNumber);
                            //
                            // If allValues is null, we are looking for one value, and we take the first one we find.
                            // If allValues is not null, then we are looking for all values we can find.
                            //
                            if(allValues == null) {
                                matchFound = true;
                                break;
                            }
                            else {
                                allValues.add(valueToReturn);
                            }
                        }
                    }
                    if(matchFound) {
                        break;
                    }
                }
                else {
                    List<String>      values;
                    Iterator          valuesIterator;
                    List<Integer>     segmentList = new ArrayList<Integer>();

                    values = getAllFieldAndSubFieldValues(segmentName, currentSegement, idFieldNumber);
                    valuesIterator = values.iterator();
                    while(valuesIterator.hasNext()) {
                        String      fieldValue = (String)valuesIterator.next();

                        valueToCheck = getSubFieldValue(fieldValue, idFieldNumber);
                        if(valueToCheck.equals(matchValue)) {
                            segmentIndexList.clear();
                            segmentIndexList.add(new Integer(currentSegement));
                            valueToReturn = getFieldValue(segmentName, segmentIndexList, fieldNumber);
                        //
                        // If allValues is null, we are looking for one value, and we take the first one we find.
                        // If allValues is not null, then we are looking for all values we can find.
                        //
                            if(allValues == null) {
                                matchFound = true;
                                break;
                            }
                            else {
                                allValues.add(valueToReturn);
                            }
                        }
                    }
                }
            }

            if(matchFound == false) {
                return "";
            }
            else {
                return valueToReturn;
            }
        }

        return "";
    }

    private List<Integer> getSegmentIndexes(String  segmentName, List<Element> identifiers) {
        List<Integer>     listOfIndexes = new ArrayList<Integer>();
        List<Integer>     segmentIndexList = new ArrayList<Integer>();
        List<String>      listOfSegments;
        Iterator          identifierIterators;
        String            fieldNumber;
        String            valueToMatch;
        String            valueToCheck;


        listOfSegments = hl7DataMap.get(segmentName);
        if(listOfSegments != null) {
            for(int  segmentIndex = 0; segmentIndex < listOfSegments.size(); segmentIndex++) {
                if(identifiers.size() > 0) {
                    identifierIterators = identifiers.iterator();
                    while(identifierIterators.hasNext()) {
                        Element       identifier = (Element)identifierIterators.next();

                        fieldNumber = identifier.getAttributeValue(XMLDefs.FIELD_NUMBER, identifier.getNamespace());

                        valueToMatch = identifier.getText();
                        segmentIndexList.clear();
                        segmentIndexList.add(new Integer(segmentIndex));
                        valueToCheck = getFieldValue(segmentName, segmentIndexList, fieldNumber);

                        if(valueToCheck.equalsIgnoreCase(valueToMatch)) {
                            listOfIndexes.add(new Integer(segmentIndex));
                        }
                    }
                }
                else {
                    listOfIndexes.add(new Integer(segmentIndex));
                }
            }
        }

        return listOfIndexes;
    }

    protected ArrayList<String>  getAllFieldValues(String segmentName, List<Integer>  segmentIndexList, String fieldNumber) {
        return  getAllFieldValues(segmentName, segmentIndexList, fieldNumber, false);
    }

    protected ArrayList<String>  getAllFieldAndSubFieldValues(String segmentName, int currentSegement, String fieldNumber) {
        List<Integer>       segmentIndexList = new ArrayList<Integer>();

        segmentIndexList.add(currentSegement);
        return  getAllFieldValues(segmentName, segmentIndexList, fieldNumber, true);
    }

    private ArrayList<String>  getAllFieldValues(String segmentName, List<Integer>  segmentIndexList,
                                                 String fieldNumber, boolean  includeSubFields) {
        ArrayList<String>     values = new ArrayList<String>();
        StringTokenizer       strtok;
        Iterator              fieldValueItertor;


        fieldValueItertor = getFieldValues(segmentName, segmentIndexList,
                                           String.valueOf(getFieldNumber(fieldNumber)), true).iterator();
        while(fieldValueItertor.hasNext()) {
            String fieldValue = (String)fieldValueItertor.next();

            strtok = new StringTokenizer(fieldValue, repeatingFieldSeparator,     true);

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
                if(includeSubFields) {
                    values.add(data);
                }
                else {
                    values.add(getSubFieldValue(data, fieldNumber));
                }
            }
        }

        return values;
    }


    protected String  getFieldValue(String segmentName, List<Integer>  occuranceList, String fieldNumber) {
        List<String>    returnedList;
        returnedList = getFieldValues(segmentName, occuranceList, fieldNumber);

        if(returnedList.size() == 0) {
            return "";
        }
        else {
            return returnedList.get(0);
        }
    }

    protected List<String>  getFieldValues(String segmentName, List<Integer>  occuranceList, String fieldNumber) {
        return getFieldValues(segmentName, occuranceList, fieldNumber, false);
    }

    protected List<String>  getFieldValues(String segmentName, List<Integer>  occuranceList,
                                    String fieldNumber, boolean  returnAllSubFields) {
        List<String>          valuesList = new ArrayList<String>();
        StringTokenizer       strtok;
        String                segment;
        int                   fieldNumberInt;
        String                value;
        ArrayList             listOfSegments;
        Iterator              occuranceListIterator;

        listOfSegments = hl7DataMap.get(segmentName);
        if(listOfSegments == null) {
            return valuesList;
        }

        occuranceListIterator = occuranceList.iterator();
        while(occuranceListIterator.hasNext()) {
            Integer       occurance = (Integer)occuranceListIterator.next();

            segment = (String)listOfSegments.get(occurance.intValue());
            if(segment == null) {
                continue;
            }

            fieldNumberInt = getFieldNumber(fieldNumber);
            strtok = new StringTokenizer(segment, fieldSeparator, true);
            //
            // Eat the segment name and the following separator.
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
                else if(returnAllSubFields == false) {
                    if(getSubFieldNumber(fieldNumber) > -1) {
                        value = getSubFieldValue(value, fieldNumber);
                    }
                }
                valuesList.add(value);
            }
        }

        return valuesList;
    }

    protected String  getSubFieldValue(String  field, String fieldNumber) {
        StringTokenizer       strtok;
        int                   subFieldNumber;
        int                   repeatingFieldSeparatorIndex;
        String                value;

        subFieldNumber = getSubFieldNumber(fieldNumber);
        if(subFieldNumber == -1) {
            return field;
        }

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
            if((repeatingFieldSeparatorIndex = value.indexOf(repeatingFieldSeparator)) != -1) {
                value = value.substring(0, repeatingFieldSeparatorIndex);
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
