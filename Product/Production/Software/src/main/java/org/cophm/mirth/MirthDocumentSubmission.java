/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cophm.mirth;

import com.mirth.connect.connectors.ws.AcceptMessage;
import com.mirth.connect.connectors.ws.WebServiceMessageReceiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.jws.WebService;
import javax.xml.ws.BindingType;

import javax.ejb.Stateless;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.cophm.util.*;
import org.cophm.validation.ErrorSeverity;
import org.cophm.validation.HL7Validator;
import org.cophm.validation.ValidationResult;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "AdapterXDR_Service", portName = "AdapterXDR_Port", endpointInterface = "gov.hhs.fha.nhinc.adapterxdr.AdapterXDRPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterxdr", wsdlLocation = "wsdl/AdapterComponentXDR.wsdl")
@BindingType(value="http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
@Stateless
public class MirthDocumentSubmission  extends AcceptMessage{
    private org.apache.log4j.Logger       logger;

    public MirthDocumentSubmission(WebServiceMessageReceiver webServiceMessageReceiver) {
       super(webServiceMessageReceiver);
       logger = this.getLogger();
    }
    public MirthDocumentSubmission()
    {
        super(null);
        logger = this.getLogger();
    }
    public RegistryResponseType provideAndRegisterDocumentSetb(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType body) {
        //TODO implement this method
        String                                                message = "";
        String                                                responseMsg = "";
        org.cophm.validation.HL7Validator                     validator = null;
        ProvideAndRegisterDocumentSetRequestType.Document     documentBase64;
        String                                                document;
        Document                                              transmittedDocument;
        Element                                               transmittedRootElement;
        Element                                               provideAndRegisterDocumentSetRequestElement;
        SAXBuilder                                            builder = new SAXBuilder();
        String                                                base64EncodedData;
        String                                                decodedData;


        logger.info("Begin provideAndRegisterDocumentSetb");

        DocumentSubmissionHelper    helper = new DocumentSubmissionHelper();
        RegistryErrorList           errorList = helper.validateDocumentMetaData(body.getProvideAndRegisterDocumentSetRequest());

        RegistryResponseType    result = null;

        if(errorList.getHighestSeverity().equals(helper.XDS_ERROR_SEVERITY_ERROR))
        {
            result = helper.createErrorResponse(errorList);
        }
        else
        {
            message = helper.buildProvideAndRegisterDocumentSetRequestXML(body);

            result = helper.createPositiveAck();

            try {
                validator = new HL7Validator(PropertyAccessor.getProperty("HOLD_DIRECTORY"),
                                             PropertyAccessor.getProperty("REPORT_DIRECTORY"),
                                             PropertyAccessor.getProperty("SCHIEPPH_VALIDATION_RULES"));

                transmittedDocument = builder.build(new StringReader(message));
                transmittedRootElement = transmittedDocument.getRootElement();

                provideAndRegisterDocumentSetRequestElement = transmittedRootElement.getChild("ProvideAndRegisterDocumentSetRequest", transmittedRootElement.getNamespace());

                base64EncodedData = provideAndRegisterDocumentSetRequestElement.getChildText("Document", transmittedRootElement.getNamespace("ns6"));

                decodedData = new String(Base64Coder.decode(base64EncodedData));




                validator.loadData(decodedData);
                if(validator.validateData() == true) {
//                    String              destinationDirectory;
//                    File                file;
//                    FileOutputStream    dataOutput;
//                    String              filePrefix;
//                    String              fileExtension;
//                    SimpleDateFormat    df = new SimpleDateFormat(Constants.OUTPUT_FILE_DATE_FORMAT);
//
//
//                    destinationDirectory = PropertyAccessor.getProperty(Constants.VALIDATED_DATA_OUTPUT_DIRECTORY);
//
//                    filePrefix = PropertyAccessor.getProperty(Constants.VALIDATED_DATA_FILE_PREFIX);
//                    fileExtension = PropertyAccessor.getProperty(Constants.VALIDATED_DATA_FILE_EXTENSION);
//
//                    file = new File(destinationDirectory);
//
//                    //
//                    // Make sure the output directory exists.
//                    //
//                    file.mkdirs();
//
//                    dataOutput = new FileOutputStream(destinationDirectory + File.separator + filePrefix + df.format(new Date()) + fileExtension);
//
//                    dataOutput.write(decodedData.getBytes());
//
//                    dataOutput.close();
//
                    this.webServiceMessageReceiver.processData(message);

                    result = helper.createPositiveAck();
                }
                else {
                    Iterator              errorIterator = validator.getErrorObjects().iterator();
                    RegistryErrorList     errorResult = new RegistryErrorList();
                    RegistryError         error;

                    while(errorIterator.hasNext()) {
                        org.cophm.validation.ValidationResult     validationResult = (ValidationResult)errorIterator.next();

                        if(validationResult.getSeverity().ordinal() == ErrorSeverity.NONE.ordinal()) {
                            continue;
                        }
                        else {
                            error = helper.createRegistryError(validationResult.getErrorCode(),
                                                               validationResult.getSeverity().name(),
                                                               validationResult.getErrorMessage());

                            errorResult.getRegistryError().add(error);
                        }

                    }
                    return helper.createErrorResponse(errorResult);
                }

            }
            catch(PropertyAccessException e) {
                RegistryErrorList     errorResult = new RegistryErrorList();
                RegistryError         error;

                logger.error("Caught a " + e.getClass().getName() + ": " + e.toString());


                error = helper.createRegistryError(DocumentSubmissionHelper.XDR_EC_XDSRegistryMetadataError,
                                                                 DocumentSubmissionHelper.XDS_ERROR_SEVERITY_ERROR,
                                                                 e.getMessage());

                errorResult.getRegistryError().add(error);

                return helper.createErrorResponse(errorResult);
            }
            catch(org.cophm.validation.HL7ValidatorException e) {
                RegistryErrorList     errorResult = new RegistryErrorList();
                RegistryError         error;

                logger.error("Caught a " + e.getClass().getName() + ": " + e.toString());


                error = helper.createRegistryError(DocumentSubmissionHelper.XDR_EC_XDSRegistryMetadataError,
                                                                 DocumentSubmissionHelper.XDS_ERROR_SEVERITY_ERROR,
                                                                 e.getMessage());

                errorResult.getRegistryError().add(error);

                return helper.createErrorResponse(errorResult);
            }
            catch(IOException e) {
                RegistryErrorList     errorResult = new RegistryErrorList();
                RegistryError         error;

                logger.error("Caught a " + e.getClass().getName() + ": " + e.toString());


                error = helper.createRegistryError(DocumentSubmissionHelper.XDR_EC_XDSRegistryMetadataError,
                                                                 DocumentSubmissionHelper.XDS_ERROR_SEVERITY_ERROR,
                                                                 e.getMessage());

                errorResult.getRegistryError().add(error);

                return helper.createErrorResponse(errorResult);
            }
            catch(JDOMException e) {
                RegistryErrorList     errorResult = new RegistryErrorList();
                RegistryError         error;

                logger.error("Caught a " + e.getClass().getName() + ": " + e.toString());


                error = helper.createRegistryError(DocumentSubmissionHelper.XDR_EC_XDSRegistryMetadataError,
                                                                 DocumentSubmissionHelper.XDS_ERROR_SEVERITY_ERROR,
                                                                 e.getMessage());

                errorResult.getRegistryError().add(error);

                return helper.createErrorResponse(errorResult);
            }

        }

        return result;
    }


    protected org.apache.log4j.Logger getLogger()
    {
        return org.apache.log4j.Logger.getLogger(this.getClass());
    }


}
