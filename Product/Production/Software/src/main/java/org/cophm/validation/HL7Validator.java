package org.cophm.validation;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import com.sun.org.apache.xml.internal.utils.NameSpace;
import org.apache.log4j.Logger;
import org.cophm.util.Base64Coder;
import org.cophm.util.Constants;
import org.cophm.util.PropertyAccessException;
import org.cophm.util.PropertyAccessor;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.*;
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

    private org.cophm.validation.ErrorSeverity      maxErrorSeverity = org.cophm.validation.ErrorSeverity.NONE;

    private String      hl7VersionNumber = "unset";
    private String      hl7MessageId = "unset";
    private String      hl7MessageType = "unset";

    private Logger      log = Logger.getLogger(this.getClass().getName());

    private String      reportDirectory;
    private String      holdDirectory;

    private IDataParser       dataParser;

    public HL7Validator(String  _holdDirectory, String  _reportDirectory) {
        holdDirectory = _holdDirectory;
        reportDirectory = _reportDirectory;
    }

    public HL7Validator(String  _holdDirectory, String  _reportDirectory, String  _validationRules)
            throws JDOMException, IOException, PropertyAccessException {
        String      fullyQualifiedFileName;

        holdDirectory = _holdDirectory;
        reportDirectory = _reportDirectory;

        fullyQualifiedFileName = PropertyAccessor.getProperty("SCHIEPPH_PROPERTIES_DIR", "conf") +
                File.separator + _validationRules;

        loadValidationRules(fullyQualifiedFileName);
    }

    public void loadValidationRules(String  fileName)
            throws JDOMException, IOException, PropertyAccessException {
        SAXBuilder      builder = new SAXBuilder();
        Iterator        errorMessageIterator;
        Iterator        errorCodeIterator;

        validationRules = builder.build(new File(fileName));

        rootElement = validationRules.getRootElement();

        errorMessageIterator = rootElement.getChild(Constants.ERROR_MESSAGES,
                                                       rootElement.getNamespace()).getChildren(Constants.ERROR_MESSAGE,
                                                       rootElement.getNamespace()).iterator();

        errorCodeIterator = rootElement.getChild(Constants.ERROR_CODES,
                                                       rootElement.getNamespace()).getChildren(Constants.ERROR_CODE,
                                                       rootElement.getNamespace()).iterator();

        validationResultsList.clear();
        errorMessageMap.clear();
        errorCodeMap.clear();
        maxErrorSeverity = org.cophm.validation.ErrorSeverity.NONE;

        while(errorMessageIterator.hasNext()) {
            Element       message = (Element)errorMessageIterator.next();

            errorMessageMap.put(message.getAttributeValue(Constants.ID, message.getNamespace()),
                                new ErrorMessageContainer(message));
        }

        while(errorCodeIterator.hasNext()) {
            Element       errorCode = (Element)errorCodeIterator.next();

            errorCodeMap.put(errorCode.getAttributeValue(Constants.ID, errorCode.getNamespace()),
                                errorCode.getText());
        }
    }


    public org.cophm.validation.ErrorSeverity getMaxErrorSeverity() {
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

    public boolean  validateData(String  inputData)
            throws HL7ValidatorException, IOException, JDOMException {
        Element     fieldsElement;
        Element     validVersionElement;
        Element     validMessageTypeElement;
        Iterator    versionNumberIterator;
        Iterator    messageTypeIterator;
        boolean     versionIsValid = false;
        boolean     messageTypeIsValid = false;
        Date        reportDate = new Date();

        if(validationRules == null) {
            throw new HL7ValidatorException("No validation rules have been loaded.");
        }

        validationResultsList.clear();
        maxErrorSeverity = org.cophm.validation.ErrorSeverity.NONE;

        validVersionElement = rootElement.getChild(Constants.VALID_HL7_VERSIONS, rootElement.getNamespace());
        validMessageTypeElement = rootElement.getChild(Constants.SUPPORTED_ADT_MESSAGES, rootElement.getNamespace());

        if(inputIsXML(inputData)) {
            dataParser = new XmlParser();
        }
        else {
            dataParser = new PipeParser();
        }


        dataParser.loadData(inputData);

        hl7VersionNumber = dataParser.getFieldValue("MSH", "12");
        hl7MessageId = dataParser.getFieldValue("MSH", "10");
        hl7MessageType = dataParser.getFieldValue("MSH", "9.2");

        versionNumberIterator = validVersionElement.getChildren(Constants.VERSION, validVersionElement.getNamespace()).iterator();
        while(versionNumberIterator.hasNext()) {
            Element       version = (Element)versionNumberIterator.next();

            if(hl7VersionNumber.equalsIgnoreCase(version.getTextTrim())) {
                versionIsValid = true;
                break;
            }
        }

        if(versionIsValid == false) {
            captureError(validVersionElement,
                         validVersionElement.getChildText(Constants.NAME, validVersionElement.getNamespace()),
                         hl7VersionNumber);
            saveData(reportDate, inputData);
            saveReport(reportDate);

            return false;
        }

        messageTypeIterator = validMessageTypeElement.getChildren(Constants.ADT_MESSAGE, validVersionElement.getNamespace()).iterator();
        while(messageTypeIterator.hasNext()) {
            Element       messageType = (Element)messageTypeIterator.next();

            if(hl7MessageType.equalsIgnoreCase(messageType.getTextTrim())) {
                messageTypeIsValid = true;
                break;
            }
        }

        if(messageTypeIsValid == false) {
            captureError(validMessageTypeElement,
                         validMessageTypeElement.getChildText(Constants.NAME, validMessageTypeElement.getNamespace()),
                         hl7MessageType);
            saveData(reportDate, inputData);
            saveReport(reportDate);

            return false;
        }

        fieldsElement = rootElement.getChild(Constants.FIELDS, rootElement.getNamespace());
        validateFieldData(fieldsElement);

        saveReport(reportDate);

        if(maxErrorSeverity == org.cophm.validation.ErrorSeverity.NONE) {
            return true;
        }
        else {
            saveData(reportDate, inputData);
            return false;
        }
    }

    private boolean inputIsXML(String data) {
        if(data.indexOf("<MSH.12") != -1 ||
            data.indexOf("<Msh.12") != -1 ||
            data.indexOf("<msh.12 ") != -1) {
            return true;
        }

        return false;
    }

    private void saveData(Date  reportDate, String data) throws IOException {
        File                file;
        FileOutputStream    output;
        SimpleDateFormat    df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss-SSS");

        file = new File(holdDirectory);
        file.mkdirs();

        file = new File(holdDirectory + File.separatorChar
                                + Constants.DATA_PREFIX + hl7MessageId + "_"
                                + df.format(reportDate)
                                + (inputIsXML(data) ? Constants.XML_DATA_POSTFIX : Constants.PIPE_DELIMITED_DATA_POSTFIX));


        output = new FileOutputStream(file);
        output.write(data.getBytes());
        output.close();

        return;
    }

    private void saveReport(Date  reportDate) throws IOException {
        File                file;
        FileOutputStream    output;
        SimpleDateFormat    df = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss-SSS");
        SimpleDateFormat    dfDisplay = new SimpleDateFormat("dd MMM yyyy  hh-mm-ss a");
        Iterator            messageIterator;
        String              reportHeader;

        if(validationResultsList.size() > 0) {
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

        fieldIterator = fieldsElement.getChildren(Constants.FIELD, fieldsElement.getNamespace()).iterator();
        while(fieldIterator.hasNext()) {
            Element       field = (Element)fieldIterator.next();
            String        fieldValue = null;

            try {
                fieldValue = dataParser.getFieldValue(getLocationElement(field));
                validateFieldUsage(fieldValue,
                                   field.getChild(Constants.VALIDATIONS,
                                                  fieldsElement.getNamespace())
                                           .getChild(Constants.USAGE,
                                                     fieldsElement.getNamespace()),
                                   field.getChildText(Constants.NAME, field.getNamespace()));
            }
            catch(HL7Exception e) {
                captureError(field.getChild(Constants.VALIDATIONS,
                                                  fieldsElement.getNamespace())
                                           .getChild(Constants.USAGE,
                                                     fieldsElement.getNamespace()),
                                                     field.getChildText(Constants.NAME,
                                                                        field.getNamespace()),
                                                     field.getText().trim());
//                captureError(getErrorCode(field.getChild(Constants.VALIDATIONS,
//                                                  fieldsElement.getNamespace())
//                                           .getChild(Constants.USAGE,
//                                                     fieldsElement.getNamespace())),
//                             field.getChildText(Constants.NAME, field.getNamespace()),
//                             e.getMessage(), org.cophm.validation.ErrorSeverity.FATAL);
                log.error("Caught a " + e.getClass().getName() + ": " + e.toString());
            }
        }
    }

    private void validateFieldUsage(String  value, Element usageElement, String  name) {
        Usage     usage;


        usage = getUsage(usageElement);

        if(value == null) {
            if(usage.ordinal() == Usage.Optional.ordinal()) {
                //
                // Missing Optional fields are not an error;
                //
                return;
            }
            if(usage.ordinal() == Usage.Required.ordinal() ||
                    usage.ordinal() == Usage.RequiredEmpty.ordinal()) {
                captureError(usageElement, name, null);
                return;
            }
        }
        else {
            if(value.trim().length() == 0) {
                if(usage.ordinal() == Usage.Required.ordinal() ||
                    usage.ordinal() == Usage.RequiredEmpty.ordinal()) {
                    captureError(usageElement, name, null);
                    return;
                }
            }
        }
    }

    private Element getLocationElement(Element fieldElement)
            throws HL7ValidatorException {
        Iterator    locationIterator;

        locationIterator = fieldElement.getChildren(Constants.LOCATION, fieldElement.getNamespace()).iterator();
        while(locationIterator.hasNext()) {
            Element       location = (Element)locationIterator.next();

            if(location.getAttributeValue(Constants.VERSION,
                                     location.getNamespace()).toString().equals(hl7VersionNumber)) {
                return location;
            }
        }

        throw new HL7ValidatorException("Could not find a location element for " +
                                        fieldElement.getChildText(Constants.NAME, fieldElement.getNamespace()) +
                                        ".  HL7       version = " + hl7VersionNumber);
    }

    private void captureError(Element validationElement, String fieldName, String fieldValue) {
        ErrorSeverity             severity;
        ErrorMessageContainer     container;

        severity = getSeverity(validationElement);

        container = errorMessageMap.get(validationElement.getAttributeValue(Constants.ERROR_MESSAGE_ID));

        ValidationResult    vr = new ValidationResult(getErrorCode(validationElement),
                (container.isPrintFieldName() ? fieldName + " " : "") +
                (container.isPrintFieldValue() ? "[value = " + fieldValue + "] " : "") +
                container.getMessage(),
                fieldName, severity);

        validationResultsList.add(vr);

        if(maxErrorSeverity.ordinal() < severity.ordinal()) {
            maxErrorSeverity = severity;
        }

        return;
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

        return errorCodeMap.get(validationElement.getAttributeValue(Constants.ERROR_CODE_ID,
                                                                    validationElement.getNamespace(), ""));
    }


    private boolean printFieldName(Element validationElement) {

        return Boolean.valueOf(validationElement.getAttributeValue(Constants.PRINT_FIELD_NAME,
                                                          validationElement.getNamespace(), "false"));
    }

    private boolean printFieldValue(Element validationElement) {

        return Boolean.valueOf(validationElement.getAttributeValue(Constants.PRINT_FIELD_VALUE,
                                                          validationElement.getNamespace(), "false"));
    }

    private ErrorSeverity getSeverity(Element validationElement) {
        String      severityStr;

        severityStr = validationElement.getAttributeValue(Constants.SEVERITY,
                                                          validationElement.getNamespace(), "None");

        if(severityStr.equalsIgnoreCase(org.cophm.validation.ErrorSeverity.FATAL.name())) {
            return org.cophm.validation.ErrorSeverity.FATAL;
        }
        else if(severityStr.equalsIgnoreCase(org.cophm.validation.ErrorSeverity.HOLD.name())) {
            return org.cophm.validation.ErrorSeverity.HOLD;
        }
        else if(severityStr.equalsIgnoreCase(org.cophm.validation.ErrorSeverity.REPORT.name())) {
            return org.cophm.validation.ErrorSeverity.REPORT;
        }
        else {
            return org.cophm.validation.ErrorSeverity.NONE;
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
        else {
            return Usage.Optional;
        }
    }

}
