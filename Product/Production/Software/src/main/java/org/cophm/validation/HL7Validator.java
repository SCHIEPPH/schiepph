package org.cophm.validation;

import org.apache.log4j.Logger;
import org.cophm.util.Constants;
import org.cophm.util.PropertyAccessException;
import org.cophm.util.PropertyAccessor;
import org.cophm.util.XMLDefs;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by
 * User: ralph
 * Date: 4/2/12
 * Time: 3:04 PM
 */
public class HL7Validator {

    private Document    validationRules = null;
    private Element     rootElement = null;

    private HashMap<String, ErrorMessageContainer>      errorMessageMap = new HashMap<String, ErrorMessageContainer>();
    private HashMap<String, String>                     errorCodeMap = new HashMap<String, String>();
    private ArrayList<ValidationResult>                 validationResultsList = new ArrayList<ValidationResult>();

    private ErrorSeverity     maxErrorSeverity = ErrorSeverity.NONE;

    private String      hl7VersionNumber = "unset";
    private String      hl7MessageId = "unset";
    private String      hl7MessageType = "unset";

    private Logger      log = Logger.getLogger(this.getClass().getName());

    private String      reportDirectory = null;
    private String      holdDirectory = null;

    private String      inputData;

    private IDataParser                   dataParser;
    private HashMap<String, Element>      validationFieldByNameCache = new HashMap<String, Element>();
    private HashMap<Integer, Element>     validationFieldByIdCache = new HashMap<Integer, Element>();

    private String[]    allDateFormats = new String[0];


    public HL7Validator(String  _holdDirectory, String  _reportDirectory) {
        holdDirectory = _holdDirectory;
        reportDirectory = _reportDirectory;
    }

    public HL7Validator(String  _holdDirectory, String  _reportDirectory, String  _validationRules)
            throws JDOMException, IOException, PropertyAccessException, HL7ValidatorException {
        String      fullyQualifiedFileName;

        holdDirectory = _holdDirectory;
        reportDirectory = _reportDirectory;

        fullyQualifiedFileName = PropertyAccessor.getProperty("SCHIEPPH_PROPERTIES_DIR", "conf") +
                File.separator + _validationRules;

        loadValidationRules(fullyQualifiedFileName);
    }

    public void loadValidationRules(String  fileName)
            throws JDOMException, IOException, PropertyAccessException, HL7ValidatorException {
        SAXBuilder      builder = new SAXBuilder();
        Iterator        errorMessageIterator;
        Iterator        errorCodeIterator;
        Iterator        validDateIterator;
        Element         dateFormatsElement;
        List            dateFormatsList;
        Iterator        fieldIterator;
        Integer         fieldId;
        int             adfIndex;

        validationRules = builder.build(new File(fileName));

        rootElement = validationRules.getRootElement();

        errorMessageIterator = rootElement.getChild(XMLDefs.ERROR_MESSAGES,
                                                       rootElement.getNamespace()).getChildren(XMLDefs.ERROR_MESSAGE,
                                                       rootElement.getNamespace()).iterator();

        errorCodeIterator = rootElement.getChild(XMLDefs.ERROR_CODES,
                                                       rootElement.getNamespace()).getChildren(XMLDefs.ERROR_CODE,
                                                       rootElement.getNamespace()).iterator();

        validationResultsList.clear();
        errorMessageMap.clear();
        errorCodeMap.clear();
        maxErrorSeverity = ErrorSeverity.NONE;

        while(errorMessageIterator.hasNext()) {
            Element       message = (Element)errorMessageIterator.next();

            errorMessageMap.put(message.getAttributeValue(XMLDefs.ID, message.getNamespace()),
                                new ErrorMessageContainer(message));
        }

        while(errorCodeIterator.hasNext()) {
            Element       errorCode = (Element)errorCodeIterator.next();

            errorCodeMap.put(errorCode.getAttributeValue(XMLDefs.ID, errorCode.getNamespace()),
                                errorCode.getText());
        }

        //
        // Cache the validation fields so we can use that data to quickly get the value
        // of a field.
        //
        validationFieldByNameCache.clear();
        fieldIterator = rootElement.getChild(XMLDefs.FIELDS, rootElement.getNamespace())
                .getChildren(XMLDefs.FIELD, rootElement.getNamespace()).iterator();
        while(fieldIterator.hasNext()) {
            Element       fieldElement = (Element)fieldIterator.next();

            validationFieldByNameCache.put(fieldElement.getChildText(XMLDefs.NAME, fieldElement.getNamespace()),
                                     fieldElement);
            fieldId = new Integer(fieldElement.getAttributeValue(XMLDefs.ID, fieldElement.getNamespace()));
            if(validationFieldByIdCache.containsKey(fieldId)) {
                throw new HL7ValidatorException("Duplicate field id attribute [" + fieldId.toString() +
                                                        "] found in " + fileName + ".");
            }
            validationFieldByIdCache.put(fieldId, fieldElement);
        }

        //
        // Load the valid date formats.
        //
        dateFormatsElement = rootElement.getChild(XMLDefs.DATE_FORMATS, rootElement.getNamespace());
        if(dateFormatsElement != null) {
            dateFormatsList = dateFormatsElement.getChildren(XMLDefs.FORMAT,
                                                             dateFormatsElement.getNamespace());
            allDateFormats = new String[dateFormatsList.size()];
            validDateIterator = dateFormatsList.iterator();
            adfIndex = 0;
            while(validDateIterator.hasNext()) {
                Element       dateFormat = (Element)validDateIterator.next();

                allDateFormats[adfIndex++] = dateFormat.getText().trim();
            }
        }
    }


    public ErrorSeverity setMaxErrorSeverity(ErrorSeverity  severity) {
        return maxErrorSeverity = severity;
    }

    public ErrorSeverity getMaxErrorSeverity() {
        return maxErrorSeverity;
    }

    public ArrayList<String>  getErrorMessages() {
        ArrayList<String>     errorMessages = new ArrayList<String>();
        Iterator              resultsIterator;

        resultsIterator = validationResultsList.iterator();
        while(resultsIterator.hasNext()) {
            ValidationResult    result = (ValidationResult)resultsIterator.next();
            String              errorCode;

            errorCode = result.getErrorCode();
            if(errorCode.length() > 0) {
                errorMessages.add(new String(errorCode + ": "  + result.getErrorMessage()));
            }
        }

        return errorMessages;
    }

    public ArrayList<ValidationResult>  getErrorObjects() {

        return validationResultsList;
    }

    public void  loadData(String  _inputData)
            throws HL7ValidatorException, IOException {

        if(validationRules == null) {
            throw new HL7ValidatorException("No validation rules have been loaded.");
        }

        inputData = _inputData;

        validationResultsList.clear();
        maxErrorSeverity = ErrorSeverity.NONE;

        dataParser = Parser.getParser(inputData);

        dataParser.loadData(inputData);

        hl7VersionNumber = dataParser.getFieldValue("MSH", "12");
        hl7MessageId = dataParser.getFieldValue("MSH", "10");
        hl7MessageType = dataParser.getFieldValue("MSH", "9.2");

        return;
    }

    public String  getMessageId() {
        return hl7MessageId;
    }

    public String  getFieldValueByName(String  fieldName)
            throws HL7ValidatorException {
        Element       fieldElement;
        List          locationList;

        fieldElement = validationFieldByNameCache.get(fieldName);
        if(fieldElement == null) {
            return "";
        }

        locationList = getLocationElement(fieldElement);

        return dataParser.getFieldValue(locationList);
    }

    public String  getFieldValueById(Integer  fieldId)
            throws HL7ValidatorException {
        Element       fieldElement;
        List          locationList;

        fieldElement = validationFieldByIdCache.get(fieldId);
        if(fieldElement == null) {
            return "";
        }

        locationList = getLocationElement(fieldElement);

        return dataParser.getFieldValue(locationList);
    }

    public boolean validateData()
            throws HL7ValidatorException, IOException {
        Element     fieldsElement;
        Element     validVersionElement;
        Element     validMessageTypeElement;
        Iterator    versionNumberIterator;
        Iterator    messageTypeIterator;
        boolean     versionIsValid = false;
        boolean     messageTypeIsValid = false;
        Date        reportDate = new Date();

        validVersionElement = rootElement.getChild(XMLDefs.VALID_HL7_VERSIONS, rootElement.getNamespace());
        validMessageTypeElement = rootElement.getChild(XMLDefs.SUPPORTED_ADT_MESSAGES, rootElement.getNamespace());

        versionNumberIterator = validVersionElement.getChildren(XMLDefs.VERSION, validVersionElement.getNamespace()).iterator();
        while(versionNumberIterator.hasNext()) {
            Element       version = (Element)versionNumberIterator.next();

            if(hl7VersionNumber.equalsIgnoreCase(version.getTextTrim())) {
                versionIsValid = true;
                break;
            }
        }

        if(versionIsValid == false) {
            captureError(validVersionElement,
                         validVersionElement.getChildText(XMLDefs.NAME, validVersionElement.getNamespace()),
                         hl7VersionNumber,      null);
            saveData(reportDate, inputData);
            saveReport(reportDate);

            return false;
        }

        messageTypeIterator = validMessageTypeElement.getChildren(XMLDefs.ADT_MESSAGE, validVersionElement.getNamespace()).iterator();
        while(messageTypeIterator.hasNext()) {
            Element       messageType = (Element)messageTypeIterator.next();

            if(hl7MessageType.equalsIgnoreCase(messageType.getTextTrim())) {
                messageTypeIsValid = true;
                break;
            }
        }

        if(messageTypeIsValid == false) {
            captureError(validMessageTypeElement,
                         validMessageTypeElement.getChildText(XMLDefs.NAME, validMessageTypeElement.getNamespace()),
                         hl7MessageType,    null);
            saveData(reportDate, inputData);
            saveReport(reportDate);

            return false;
        }

        fieldsElement = rootElement.getChild(XMLDefs.FIELDS, rootElement.getNamespace());
        validateFieldData(fieldsElement);

        saveReport(reportDate);

        if(maxErrorSeverity.ordinal()
                <= ErrorSeverity.REPORT.ordinal()) {
            return true;
        }
        else {
            saveData(reportDate, inputData);
            return false;
        }
    }

    private void saveData(Date  reportDate, String data) throws IOException {
        File                file;
        FileOutputStream    output;
        SimpleDateFormat    df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss-SSS");

        if(holdDirectory != null) {
            file = new File(holdDirectory);
            file.mkdirs();

            file = new File(holdDirectory + File.separatorChar
                                    + Constants.DATA_PREFIX + hl7MessageId + "_"
                                    + df.format(reportDate)
                                    + (Parser.inputIsXML(data) ? Constants.XML_DATA_POSTFIX : Constants.PIPE_DELIMITED_DATA_POSTFIX));

            output = new FileOutputStream(file);
            output.write(data.getBytes());
            output.close();
        }

        return;
    }

    private void saveReport(Date  reportDate) throws IOException {
        File                file;
        FileOutputStream    output;
        SimpleDateFormat    df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss-SSS");
        SimpleDateFormat    dfDisplay = new SimpleDateFormat("dd MMM yyyy  hh-mm-ss a");
        Iterator            messageIterator;
        String              reportHeader;

        if(reportDirectory != null && validationResultsList.size() > 0) {
            file = new File(reportDirectory);
            file.mkdirs();

            file = new File(reportDirectory + File.separatorChar
                                    + Constants.REPORT_PREFIX + hl7MessageId + "_" +
                                    df.format(reportDate) + Constants.REPORT_POSTFIX);


            output = new FileOutputStream(file);

            reportHeader = "The following Warnings and Errors were found while validating message "
                    + hl7MessageId + " on " + dfDisplay.format(reportDate) + ":\n";
            output.write(reportHeader.getBytes());

            messageIterator = getErrorMessages().iterator();
            while(messageIterator.hasNext()) {
                String      msg = (String)messageIterator.next();

                output.write(msg.getBytes());
                output.write("\n".getBytes());
            }
            output.close();
        }

        return;
    }

    private void validateFieldData(Element fieldsElement)
            throws HL7ValidatorException {
        Iterator    fieldIterator;

        fieldIterator = fieldsElement.getChildren(XMLDefs.FIELD, fieldsElement.getNamespace()).iterator();
        while(fieldIterator.hasNext()) {
            Element       field = (Element)fieldIterator.next();

            validateField(field);
        }
    }

    private void validateField(Element  field)
            throws HL7ValidatorException {
        Element       usageElement;
        Usage         usage;
        String        fieldName;
        Element       validations;
        String        fieldValue = null;

        fieldName = field.getChildText(XMLDefs.NAME, field.getNamespace());
        fieldValue = dataParser.getFieldValue(getLocationElement(field));
        validations = field.getChild(XMLDefs.VALIDATIONS, field.getNamespace());
        if(validations == null) {
            throw new HL7ValidatorException("Field definition for " + fieldName + " does not have a " +
                                                    XMLDefs.VALIDATIONS + " element.");
        }

        usageElement = validations.getChild(XMLDefs.USAGE, field.getNamespace());

        if(usageElement != null) {
            usage = getUsage(usageElement);
        }
        else {
            usage = Usage.Optional;
        }

        validateFieldUsage(fieldValue, usageElement, fieldName, usage);

        validateFieldValue(fieldValue, validations.getChild(XMLDefs.VALUE_LIST, field.getNamespace()),
                           fieldName, usage);

        validateFieldDataType(fieldValue, validations.getChild(XMLDefs.DATA_TYPE, field.getNamespace()),
                              fieldName, usage);

        validateFieldRequiresFieldValue(fieldValue, validations.getChild(XMLDefs.REQUIRES, field.getNamespace()),
                                        fieldName, usage);

        validateFieldRequiresConditionalField(fieldValue,
                                              validations.getChild(XMLDefs.REQUIRES, field.getNamespace()),
                                              fieldName, usage);

    }

    private void validateFieldRequiresConditionalField(String fieldValue, Element requiresElement, String fieldName, Usage usage)
            throws HL7ValidatorException {
        List            conditionalFieldList;
        Iterator        conditionalFieldIterator;
        Element         validationRulesFieldElement;
        Element         validationRulesFieldElementClone;
        Element         validationsElement = null;
        Element         usageElement = null;
        String          mustExistStr;
        boolean         mustExist;
        boolean         foundConditionalField = false;


        if(fieldValue.trim().length() == 0) {
            //
            // If the field is not present, then it cannot require anything else.
            //
            return;
        }

        //
        // If the requires element is not present, then there is nothing to check.
        //
        if(requiresElement == null) {
            return;
        }

        conditionalFieldList = requiresElement.getChildren(XMLDefs.CONDITIONAL_FIELD_ID,
                                                               requiresElement.getNamespace());

        conditionalFieldIterator = conditionalFieldList.iterator();
        while(conditionalFieldIterator.hasNext()) {
            Element conditionalFieldIdElement = (Element)conditionalFieldIterator.next();

            mustExistStr = conditionalFieldIdElement.getAttributeValue(XMLDefs.MUST_EXIST, conditionalFieldIdElement
                                .getNamespace(), "true");
            mustExist = Boolean.parseBoolean(mustExistStr);

            validationRulesFieldElement = validationFieldByIdCache.get(new Integer(conditionalFieldIdElement.getText()));
            if(validationRulesFieldElement == null) {
                if(mustExist) {
                    captureError(requiresElement, fieldName, "FieldId = " + conditionalFieldIdElement.getText(), usage);
                }
            }
            else {
                foundConditionalField = true;

                //
                // Conditional fields are not validated by default, so we need to clone the validation rule field element
                // and change the Usage from conditional to required.  Allow for the condition where the validations
                // element or the usage element are missing.
                //
                validationsElement = validationRulesFieldElement.getChild(XMLDefs.VALIDATIONS, validationRulesFieldElement
                                        .getNamespace());
                if(validationsElement != null) {
                    usageElement = validationsElement.getChild(XMLDefs.USAGE, validationsElement
                                                .getNamespace());
                }

                validationRulesFieldElementClone = (Element)validationRulesFieldElement.clone();
                validationsElement = validationRulesFieldElementClone.getChild(XMLDefs.VALIDATIONS,
                                                                               validationRulesFieldElementClone
                                                                                       .getNamespace());
                if(validationsElement != null) {
                    usageElement = validationsElement.getChild(XMLDefs.USAGE, validationsElement
                                                .getNamespace());
                    if(usageElement != null) {
                        usageElement.removeAttribute(XMLDefs.SEVERITY);
                        usageElement.setAttribute(XMLDefs.SEVERITY, ErrorSeverity.HOLD.name());
                        usageElement.setText(Usage.Required.name());
                    }
                    else {
                        addRequiredUsageElement(validationsElement);
                    }
                }
                else {
                    validationsElement = new Element(XMLDefs.VALIDATIONS);
                    validationRulesFieldElementClone.addContent(validationsElement);

                    addRequiredUsageElement(validationsElement);
                }

                validateField(validationRulesFieldElementClone);
            }
        }

        if(conditionalFieldList.size() > 0 && foundConditionalField == false) {
            captureError(requiresElement, fieldName, "", usage, " Conditionally required field(s) not found.");
        }
    }

    private void addRequiredUsageElement(Element validationsElement) {
        Element     usageElement;

        usageElement = new Element(XMLDefs.USAGE);
        usageElement.setAttribute(XMLDefs.SEVERITY, ErrorSeverity.HOLD.name());
        usageElement.setAttribute(XMLDefs.ERROR_CODE_ID, "1");
        usageElement.setAttribute(XMLDefs.ERROR_MESSAGE_ID, "1");
        usageElement.setText(Usage.Required.name());

        validationsElement.addContent(usageElement);
    }

    private void validateFieldRequiresFieldValue(String fieldValue, Element requiresElement,
                                                 String fieldName, Usage usage)
            throws HL7ValidatorException {
        Iterator    fieldValueIterator;


        //
        // If the field is not present, then it cannot require anything else.
        //
        if(fieldValue.trim().length() == 0) {
            return;
        }

        //
        // If the requires element is not present, then there is nothing to check.
        //
        if(requiresElement == null) {
            return;
        }
        //
        // Process all field value elements that we find.
        //
        fieldValueIterator = requiresElement.getChildren(XMLDefs.FIELD_VALUE,
                                                         requiresElement.getNamespace()).iterator();

        while(fieldValueIterator.hasNext()) {
            Element                 fieldValueElement = (Element)fieldValueIterator.next();
            Element                 fieldRuleElement;
            Iterator                locationElementIterator;
            Element                 locationElementClone;
            ArrayList<Element>      locationElementList = new ArrayList<Element>();
            Element                 hl7SegmentElement;
            Element                 identifierElement;
            String                  requiredFieldValue;
            String                  fieldNumberStr;
            String                  valueToCheck;
            String                  mustMatchStr;
            boolean                 mustMatch;
            String                  caseSensitiveStr;
            boolean                 caseSensitive;


            requiredFieldValue = fieldValueElement.getText();
            fieldNumberStr = fieldValueElement.getAttributeValue(XMLDefs.FIELD_NUMBER,
                                                                 fieldValueElement.getNamespace(), "");

            mustMatchStr = fieldValueElement.getAttributeValue(XMLDefs.MUST_MATCH,
                                                               fieldValueElement.getNamespace(), "false");
            mustMatch = Boolean.getBoolean(mustMatchStr);

            caseSensitiveStr = fieldValueElement.getAttributeValue(XMLDefs.CASE_SENSITIVE,
                                                                 fieldValueElement.getNamespace(), "false");
            caseSensitive = Boolean.getBoolean(caseSensitiveStr);

            fieldRuleElement = (Element)requiresElement.getParent().getParent();

            locationElementIterator = getLocationElement(fieldRuleElement).iterator();
            while(locationElementIterator.hasNext()) {
                Element       locationElement = (Element)locationElementIterator.next();

                locationElementList.add(locationElement);
                if(dataParser.getFieldValue(locationElementList).length() == 0) {
                    locationElementList.clear();
                    continue;
                }

                locationElementClone = (Element)locationElement.clone();
                identifierElement = locationElementClone.getChild(XMLDefs.IDENTIFIER, locationElement.getNamespace());
                hl7SegmentElement = locationElementClone.getChild(XMLDefs.HL7_SEGMENT, locationElement.getNamespace());
                hl7SegmentElement.removeAttribute(XMLDefs.FIELD_NUMBER);
                hl7SegmentElement.setAttribute(XMLDefs.FIELD_NUMBER, fieldNumberStr);
                locationElementClone.removeContent();
                locationElementClone.addContent((Content)hl7SegmentElement);
                locationElementClone.addContent((Content)identifierElement);

                locationElementList.clear();
                locationElementList.add(locationElementClone);
            }

            valueToCheck = dataParser.getFieldValue(locationElementList);

            if(caseSensitive) {
                if(valueToCheck.equals(requiredFieldValue) == false) {
                    if(mustMatch) {
                        captureError(requiresElement, fieldName, valueToCheck, usage);
                    }
                }
                else {
                    return;
                }
            }
            else {
                if(valueToCheck.equalsIgnoreCase(requiredFieldValue) == false) {
                    if(mustMatch) {
                        captureError(requiresElement, fieldName, valueToCheck, usage);
                    }
                }
                else {
                    return;
                }
            }
        }

        captureError(requiresElement, fieldName, "", usage);

        return;
    }

    protected void validateFieldDataType(String fieldValue, Element dataTypeElement, String fieldName, Usage  usage) {
        DataType    dataType;
        String      format;
        String      specialCharactersStr;
        char[]      specialCharacters = new char[0];
        int         minimumLength;
        int         maximumLength;

        if(dataTypeElement == null) {
            return;
        }

        minimumLength = Integer.parseInt(dataTypeElement.getAttributeValue(XMLDefs.MINIMUM_LENGTH,
                                                                           dataTypeElement.getNamespace(), "-1"));

        maximumLength = Integer.parseInt(dataTypeElement.getAttributeValue(XMLDefs.MAXIMUM_LENGTH,
                                                                           dataTypeElement.getNamespace(), "-1"));

        specialCharactersStr = dataTypeElement.getAttributeValue(XMLDefs.SPECIAL_CHARACTERS, dataTypeElement.getNamespace());
        if(specialCharactersStr != null) {
            specialCharacters = specialCharactersStr.toCharArray();
        }

        format = dataTypeElement.getAttributeValue(XMLDefs.FORMAT, dataTypeElement.getNamespace());

        dataType = getDataType(dataTypeElement);

        if(checkDataLength(fieldValue, minimumLength, maximumLength) == false) {
            ErrorMessageContainer     container;

            container = errorMessageMap.get(dataTypeElement.getAttributeValue(XMLDefs.ERROR_MESSAGE_ID));

            captureError(getErrorCode(dataTypeElement), container.getMessage() + " (invalid length)",
                         fieldName, getSeverity(dataTypeElement));
            return;
        }

        switch(dataType) {
            case Alpha:
                if(dataIsAlphabetic(fieldValue, specialCharacters) == false) {
                    captureError(dataTypeElement, fieldName, fieldValue, usage);
                }
                break;
            case Numeric:
                if(dataIsNumeric(fieldValue, specialCharacters) == false) {
                    captureError(dataTypeElement, fieldName, fieldValue, usage);
                }
                break;
            case Date:
                try {
                    if(dataIsDate(fieldValue, format) == false) {
                        captureError(dataTypeElement, fieldName, fieldValue, usage);
                    }
                }
                catch(IllegalArgumentException e) {
                    captureError(dataTypeElement, fieldName, fieldValue, usage, " Invalid date format " + format);
                }
                break;
            case AlphaNumeric:
                if(dataIsAlphNumeric(fieldValue, specialCharacters) == false) {
                    captureError(dataTypeElement, fieldName, fieldValue, usage);
                }
                break;
            default:
                break;
        }
    }

    private boolean dataIsDate(String fieldValue, String format) {
        SimpleDateFormat    df = new SimpleDateFormat();
        Date                result;

        df.setLenient(false);

        if(format != null && format.length() > 0) {
            df.applyPattern(format);
            try {
                df.parse(fieldValue);
                return true;
            }
            catch(ParseException e) {
                log.error("Caught a " + e.getClass().getName() + ": " + e.toString());
            }
        }
        else {
            for(int  x = 0; x < allDateFormats.length; x++) {
                df.applyPattern(allDateFormats[x]);
                try {
                    df.parse(fieldValue);
                    return true;
                }
                catch(ParseException e) {
                    //
                    // Data did not conform to the supplied pattern, so try the next one.
                    //
                    continue;
                }
            }
        }

        //
        // The data didn't conform to the format that was passed in or, if no format was passed in
        // it didn't conform to the set of allowable date formats.
        //
        return false;
    }

    private boolean dataIsAlphNumeric(String fieldValue, char[] specialCharacters) {
        char[]      fieldValueChar = new char[0];

        if(fieldValue == null || fieldValue.length() == 0) {
            return false;
        }

        fieldValueChar = fieldValue.toCharArray();
        for(int  x = 0; x < fieldValueChar.length; x++) {
            if(Character.isLetterOrDigit(fieldValueChar[x]) == false) {
                if(isSpecialCharacter(fieldValueChar[x], specialCharacters) == false) {
                    return false;
                }
            }
        }

        return true;

    }

    private boolean dataIsNumeric(String fieldValue, char[] specialCharacters) {
        char[]      fieldValueChar = new char[0];

        if(fieldValue == null || fieldValue.length() == 0) {
            return false;
        }

        fieldValueChar = fieldValue.toCharArray();
        for(int  x = 0; x < fieldValueChar.length; x++) {
            if(Character.isDigit(fieldValueChar[x]) == false) {
                if(isSpecialCharacter(fieldValueChar[x], specialCharacters) == false) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean dataIsAlphabetic(String fieldValue, char[] specialCharacters) {
        char[]      fieldValueChar = new char[0];

        if(fieldValue == null || fieldValue.length() == 0) {
            return false;
        }

        fieldValueChar = fieldValue.toCharArray();
        for(int  x = 0; x < fieldValueChar.length; x++) {
            if(Character.isLetter(fieldValueChar[x]) == false) {
                if(isSpecialCharacter(fieldValueChar[x], specialCharacters) == false) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isSpecialCharacter(char ch, char[] specialCharacters) {

        for(int  x = 0; x < specialCharacters.length; x++) {
            if(ch == specialCharacters[x]) {
                return true;
            }
        }

        return false;
    }

    private boolean checkDataLength(String fieldValue, int minimumLength, int maximumLength) {
        int       fieldLength;

        fieldLength = fieldValue.length();

        if(minimumLength > -1) {
            if(fieldLength < minimumLength) {
                return false;
            }
        }

        if(maximumLength > -1) {
            if(fieldLength > maximumLength) {
                return false;
            }
        }

        return true;
    }

    private void validateFieldUsage(String  value, Element usageElement, String  name, Usage  usage) {

        if(value == null) {
            if(usage.ordinal() == Usage.Optional.ordinal() ||
                    usage.ordinal() == Usage.Conditional.ordinal() ||
                    usage.ordinal() == Usage.ConditionalEmpty.ordinal()) {
                //
                // Missing Optional fields are not an error.
                // Neither are missing Conditional or Conditional Empty fields.
                // Note that Conditional fields are checked when the field that
                // requires them are checked.
                //
                return;
            }
            if(usage.ordinal() == Usage.Required.ordinal() ||
                    usage.ordinal() == Usage.RequiredEmpty.ordinal()) {
                captureError(usageElement, name, null, usage);
                return;
            }
        }
        else {
            if(value.trim().length() == 0) {
                if(usage.ordinal() == Usage.Required.ordinal() ||
                    usage.ordinal() == Usage.RequiredEmpty.ordinal()) {
                    captureError(usageElement, name, null, usage);
                    return;
                }
            }
        }
    }

    protected void validateFieldValue(String  value, Element  validValues, String  name, Usage  usage) {
        Iterator    validValueIterator;
        String      caseSensitiveStr;
        boolean     caseSensitive;

        //
        // If there is no valid values list, then any value is valid.
        //
        if(validValues == null) {
            return;
        }

        caseSensitiveStr = validValues.getAttributeValue(XMLDefs.CASE_SENSITIVE, validValues.getNamespace());
        if(caseSensitiveStr == null ||  caseSensitiveStr.equalsIgnoreCase("false")) {
            caseSensitive = false;
        }
        else {
            caseSensitive = true;
        }

        validValueIterator = validValues.getChildren(XMLDefs.VALUE, validValues.getNamespace()).iterator();
        while(validValueIterator.hasNext()) {
            Element       validValueElement = (Element)validValueIterator.next();
            String        validValue;

            validValue = validValueElement.getText();
            if(caseSensitive) {
                if(validValue.equals(value)) {
                    return;
                }
            }
            else {
                if(validValue.equalsIgnoreCase(value)) {
                    return;
                }
            }
        }

        //
        // If we got here, we didn't match one of the valid values.
        //
        captureError(validValues, name, null, usage);

        return;
    }

    private List getLocationElement(Element fieldElement)
            throws HL7ValidatorException {
        Iterator                locationIterator;
        ArrayList<Element>      locations = new ArrayList<Element>();

        locationIterator = fieldElement.getChildren(XMLDefs.LOCATION, fieldElement.getNamespace()).iterator();
        while(locationIterator.hasNext()) {
            Element       location = (Element)locationIterator.next();

            if(location.getAttributeValue(XMLDefs.VERSION,
                                     location.getNamespace()).toString().equals(hl7VersionNumber)) {
                locations.add(location);
            }
        }

        if(locations.size() == 0) {
            throw new HL7ValidatorException("Could not find a location element for " +
                                            fieldElement.getChildText(XMLDefs.NAME, fieldElement.getNamespace()) +
                                            ".  HL7       version = " + hl7VersionNumber);
        }
        else {
            return locations;
        }
    }

    private void captureError(Element validationElement, String fieldName, String fieldValue, Usage  usage) {
        captureError(validationElement, fieldName, fieldValue, usage, null);
    }

    private void captureError(Element validationElement, String fieldName, String fieldValue, Usage  usage,
                              String  additionalMessageDetail) {
        ErrorSeverity             severity;
        ErrorMessageContainer     container;

        if(usageIsOptionalOrConditional(usage)) {
            return;
        }

        severity = getSeverity(validationElement);

        container = errorMessageMap.get(validationElement.getAttributeValue(XMLDefs.ERROR_MESSAGE_ID));

        ValidationResult    vr = new ValidationResult(getErrorCode(validationElement),
                (container.isPrintFieldName() ? fieldName + " " : "") +
                (container.isPrintFieldValue() ? "[value = " + fieldValue + "] " : " ") +
                container.getMessage() + (additionalMessageDetail != null ? " " + additionalMessageDetail : ""),
                fieldName,      severity);

        validationResultsList.add(vr);

        if(maxErrorSeverity.ordinal() < severity.ordinal()) {
            maxErrorSeverity = severity;
        }

        return;
    }

    private boolean usageIsOptionalOrConditional(Usage usage) {

        if(usage != null) {
            switch(usage) {
                case Optional:
                case Conditional:
                case ConditionalEmpty:
                    return true;
                default:
                    return false;
            }
        }

        return false;
    }

    private void captureError(String  errorCode, String message, String fieldName, ErrorSeverity severity) {
        ValidationResult    vr = new ValidationResult(errorCode, message, fieldName, severity);

        validationResultsList.add(vr);

        if(maxErrorSeverity.ordinal() < severity.ordinal()) {
            maxErrorSeverity = severity;
        }

        return;
    }


    private String getErrorCode(Element validationElement) {

        return errorCodeMap.get(validationElement.getAttributeValue(XMLDefs.ERROR_CODE_ID,
                                                                    validationElement.getNamespace(), ""));
    }


    private boolean printFieldName(Element validationElement) {

        return Boolean.valueOf(validationElement.getAttributeValue(XMLDefs.PRINT_FIELD_NAME,
                                                          validationElement.getNamespace(), "false"));
    }

    private boolean printFieldValue(Element validationElement) {

        return Boolean.valueOf(validationElement.getAttributeValue(XMLDefs.PRINT_FIELD_VALUE,
                                                          validationElement.getNamespace(), "false"));
    }

    private ErrorSeverity getSeverity(Element validationElement) {
        String      severityStr;

        severityStr = validationElement.getAttributeValue(XMLDefs.SEVERITY,
                                                          validationElement.getNamespace(), "None");

        if(severityStr.equalsIgnoreCase(ErrorSeverity.FATAL.name())) {
            return ErrorSeverity.FATAL;
        }
        else if(severityStr.equalsIgnoreCase(ErrorSeverity.HOLD.name())) {
            return ErrorSeverity.HOLD;
        }
        else if(severityStr.equalsIgnoreCase(ErrorSeverity.REPORT.name())) {
            return ErrorSeverity.REPORT;
        }
        else {
            return ErrorSeverity.NONE;
        }
    }

    private Usage getUsage(Element usageElement) {
        String      usageStr;

        usageStr = usageElement.getTextTrim();

        if(usageStr.equalsIgnoreCase(Usage.Required.name())) {
            return Usage.Required;
        }
        else if(usageStr.equalsIgnoreCase(Usage.RequiredEmpty.name())) {
            return Usage.RequiredEmpty;
        }
        else if(usageStr.equalsIgnoreCase(Usage.Conditional.name())) {
            return Usage.Conditional;
        }
        else if(usageStr.equalsIgnoreCase(Usage.ConditionalEmpty.name())) {
            return Usage.ConditionalEmpty;
        }
        else {
            return Usage.Optional;
        }
    }

    private DataType  getDataType(Element dataTypeElement) {
        String      dataTypeStr;

        dataTypeStr = dataTypeElement.getTextTrim();

        if(dataTypeStr.equalsIgnoreCase(DataType.Alpha.name())) {
            return DataType.Alpha;
        }
        else if(dataTypeStr.equalsIgnoreCase(DataType.Numeric.name())) {
            return DataType.Numeric;
        }
        else if(dataTypeStr.equalsIgnoreCase(DataType.Date.name())) {
            return DataType.Date;
        }
        else {
            return DataType.AlphaNumeric;
        }
    }

}
