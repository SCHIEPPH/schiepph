package org.cophm.direct;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPNestedMessage;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.BASE64DecoderStream;
import org.apache.log4j.Logger;
import org.cophm.util.Base64Coder;
import org.cophm.util.Constants;
import org.cophm.util.EmailSender;
import org.cophm.util.PropertyAccessException;
import org.cophm.validation.ErrorSeverity;
import org.cophm.validation.HL7Validator;
import org.cophm.validation.HL7ValidatorException;
import org.cophm.validation.Parser;
import org.jdom.JDOMException;
import sun.misc.BASE64Decoder;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by
 * User: ralph
 * Date: 6/28/12
 * Time: 11:33 AM
 */
public class DirectConnectReader {

    private String          url;
    private String          host;
    private int             port;
    private String          userId;
    private String          password;
    private Properties      props;

    private String      POP3_PROTOCOL = "pop3";
    private String      IMAP_PROTOCOL = "imap";
    private String      FILE_NAME = "JavaBridge/webmail";

    private String      smtpHost = "unset";
    private String      fromEmail = "unset";
    private String      adminEmail = "unset";

    private String      validationRulesFileName;

    private String      receivedMessageDirectory;
    private String      holdDirectory;
    private String      reportDirectory;

    private SimpleDateFormat    df = new SimpleDateFormat(Constants.OUTPUT_FILE_DATE_FORMAT);
    private SimpleDateFormat    logDf = new SimpleDateFormat(Constants.LOG_FILE_DATE_FORMAT);

    private String      url_path = "/JavaBridge/webmail/src/right_main.php?PG_SHOWALL=0&sort=0&startMessage=1&mailbox=INBOX";

    public static final int       BYTE_BUFFER_SIZE = 1024;

    private Logger      log = Logger.getLogger(this.getClass().getName());

    private FileOutputStream    myOutputStream;

    private ArrayList<String>     contentTypesToProcess = new ArrayList<String>();


    public DirectConnectReader(String _host, String _port, String _userId, String _password,
                               String  _receivedDirectory, String               _holdDirectory,
                               String  _reportDirectory, String                 _validationRulesFileName,
                               String  _smtpHost, String  _fromEmail, String    _adminEmail,
                               String  _contentTypesToProcess, String  _logDirectory) throws IOException {
        StringTokenizer       strtok;

        myOutputStream = new FileOutputStream(_logDirectory + File.separator + "myLogfile.log", true);

        host = _host;
        port = Integer.parseInt(_port);
        userId = _userId;
        password = _password;

        validationRulesFileName = _validationRulesFileName;

        receivedMessageDirectory = _receivedDirectory;
        holdDirectory = _holdDirectory;
        reportDirectory = _reportDirectory;

        smtpHost = _smtpHost;
        fromEmail = _fromEmail;
        adminEmail = _adminEmail;

        props = new Properties();

        props.put("protocol", IMAP_PROTOCOL);
        props.put("type", "store");
        props.put("class", "com.sun.mail.imap.IMAPStore");

        strtok = new StringTokenizer(_contentTypesToProcess, ",");
        while(strtok.hasMoreTokens()) {
            String mimeTypeToProcess = strtok.nextToken();

            contentTypesToProcess.add(mimeTypeToProcess.trim().toLowerCase());
        }
    }

    private void  myLog(String  msg) {
        try {
            myOutputStream.write(logDf.format(new Date()).getBytes());
            myOutputStream.write(msg.getBytes());
            myOutputStream.write(msg.getBytes());
            myOutputStream.write("\n".getBytes());
            myOutputStream.flush();
        }
        catch(IOException e) {
            log.error("Caught a " + e.getClass().getName() + ": " + e.toString());
        }

    }

    public void processIncommingEmail() throws IOException, MessagingException, HL7ValidatorException, JDOMException, PropertyAccessException {
        IMAPStore     store;
        ArrayList     hl7DataToPassAlong;

        store = connectToServer();

        createDirectories();

        hl7DataToPassAlong = processInbox(store, validationRulesFileName);

        //
        // Since we accept data in XML format and Pipe Delimited format, we need to add
        // code here to convert it to a single, standard format (XML) that we can pass along
        // to the rest of the "Channel chain" and not get bit by the transformation step.
        // For now, we will write the data to a directory and have the Pipe Delimited and
        // XML Adapters pick up the files, convert them to the internal format and then pass
        // it along.
        //

        return;
    }

    private IMAPStore connectToServer() throws UnknownHostException, MessagingException {
        Session                     session;
        IMAPStore                   store;
        PasswordAuthentication      pwAuth;
        URLName                     urlName;


        session = Session.getDefaultInstance(props);

        pwAuth = new PasswordAuthentication(userId, password);
        urlName = new URLName(IMAP_PROTOCOL, host, port, FILE_NAME, userId, password);
        session.setPasswordAuthentication(urlName, pwAuth);

        store = (IMAPStore)session.getStore(IMAP_PROTOCOL);

        store.connect(host, port, userId, password);

        return store;
    }

    private ArrayList  processInbox(IMAPStore  store, String  validationRulesFileName)
            throws MessagingException, IOException,       HL7ValidatorException,
            JDOMException, PropertyAccessException {
        Folder                inbox;
        Message[]             messages;
        Address[]             returnedAddresses;
        Address               replyToAddress = new InternetAddress("unset");
        Object                msg;
        int                   attachmentCount = 0;
        boolean               foundHl7Data = false;
        Object                part;
        String                msgData;
        String                msgSubject;
        String[]              reports;
        ArrayList<String>     hl7DataToPassAlong = new ArrayList<String>();
        ArrayList<String>     validationReports = new ArrayList<String>();
        HL7Validator          validator = new HL7Validator(null, null);
        EmailSender           emailUtil = new EmailSender(smtpHost, fromEmail, adminEmail);
        Iterator              messageDataIterator;

        validator.loadValidationRules(validationRulesFileName);

        inbox = store.getFolder("INBOX");

        inbox.open(Folder.READ_WRITE);

        messages = inbox.getMessages();

        for(int  x = 0; x < messages.length; x++) {
            foundHl7Data = false;
            returnedAddresses = messages[x].getReplyTo();
            if(returnedAddresses != null &&
                    returnedAddresses.length > 0 && returnedAddresses[0] != null) {
                replyToAddress = returnedAddresses[0];
            }
            else {
                returnedAddresses = messages[x].getFrom();
                if(returnedAddresses != null &&
                        returnedAddresses.length > 0 && returnedAddresses[0] != null) {
                    replyToAddress = returnedAddresses[0];
                }
                else {
                    throw new MessagingException("No ReplyTo: or From: address found in email headers.");
                }
            }

            validationReports.clear();

            msg = messages[x].getContent();
            if(msg instanceof Multipart) {
                attachmentCount = ((Multipart)msg).getCount();
                for(int y = 0; y < attachmentCount; y++) {
                    part = ((Multipart)msg).getBodyPart(y);
                    if(part instanceof MimeBodyPart) {
                        messageDataIterator = getContent((MimeBodyPart)part).iterator();
                        while(messageDataIterator.hasNext()) {
                            msgData = (String)messageDataIterator.next();
                            if(isHL7Data(msgData)) {
                                foundHl7Data = true;
                                //
                                // Need to validate the data, and send a report back to
                                // the sender.
                                //
                                try {
                                    validator.loadData(msgData);

                                    if(validator.validateData()) {
                                        //
                                        // Need to move th processed emails to a "processed" folder.
                                        //
                                        hl7DataToPassAlong.add(msgData);

                                        writeDataToProcessedDirectory(msgData, validator);
                                        if(validator.getMaxErrorSeverity().ordinal()
                                                >= ErrorSeverity.REPORT.ordinal()) {
                                            writeValidationReportToReportDirectory(validator);
                                        }
                                    }
                                    else {
                                        writeDataToHoldDirectory(msgData, validator);
                                        writeValidationReportToReportDirectory(validator);
                                    }
                                    validationReports.add(createResponseMessages(validator));
                                }
                                catch(HL7ValidatorException e) {
                                    validationReports.add("Could not parse input data: " + e.getMessage() +
                                            "  Input    data = [\n" + msgData + "]");
                                }
                                catch(IOException e) {
                                    validationReports.add("Could not parse input data: " + e.getMessage() +
                                            "  Input    data = [\n" + msgData + "]");
                                }
                                catch(IllegalArgumentException e) {
                                    validationReports.add("Could not parse input data: " + e.getMessage() +
                                            "  Input    data = [\n" + msgData + "]");
                                }
                            }
                        }
                    }
                }
            }

            messages[x].setFlag(Flags.Flag.DELETED, true);

            //
            // Send an email back to the sender with validation results.
            //

            msgSubject = messages[x].getSubject();
            if(msgSubject == null || msgSubject.trim().length() == 0) {
                msgSubject = "No Subject";
            }

            if(foundHl7Data) {
                reports = new String[validationReports.size()];

                for(int z = 0; z < reports.length; z++ ) {
                    reports[z] = validationReports.get(z);
                }
            }
            else {
                reports = new String[1];

                reports[0] = Constants.NO_HL7_DATA_FOUND_TEXT + msgSubject;
            }

            emailUtil.sendEmail(replyToAddress.toString(), Constants.RESPONSE_EMAIL_SUBJECT +
                    msgSubject, reports);
        }

        //
        // Close the INBOX and remove all messages marked as deleted.
        //
        inbox.close(true);


        return hl7DataToPassAlong;
    }

    private ArrayList<String> getContent(MimeBodyPart  bodyPart) throws IOException, MessagingException {
        ArrayList<String>     returnData = new ArrayList<String>();
        Object                content;
        Object                embeddedContent;
        Object                part;

        //
        // If the disposition is null or disposition is not "attachment"
        // then we need to ignore the content.
        //
        if(bodyPart.getDisposition() == null ||
                bodyPart.getDisposition().equalsIgnoreCase("attachment") == false) {
            return returnData;
        }

        if(contentShouldBeIgnored(bodyPart.getContentType())) {
            return returnData;
        }

        content = bodyPart.getContent();

        if(content instanceof String) {
            returnData.add((String)content);
        }
        else if(content instanceof IMAPNestedMessage) {
            int       messagePartCount = 0;
            embeddedContent = ((IMAPNestedMessage)content).getContent();
            if(embeddedContent instanceof MimeMultipart) {
                messagePartCount = ((MimeMultipart)embeddedContent).getCount();
                for(int x = 0; x < messagePartCount; x++) {
                    part = ((MimeMultipart)embeddedContent).getBodyPart(x);
                    if(contentShouldBeIgnored(((BodyPart)part).getContentType())) {
                        continue;
                    }
                    if(part instanceof String) {
                        returnData.add((String)part);
                    }
                    else if(part instanceof BASE64DecoderStream) {
                        String      base64EncodedData;

                        base64EncodedData = getContentFromInputStream(((BASE64DecoderStream)part));

                        returnData.add(base64EncodedData);
                    }
                    else if(part instanceof StreamSource) {
                        String      data;

                        data = getContentFromInputStream(((StreamSource)part).getInputStream());

                        returnData.add(data);
                    }
                    else {
                      myLog("Fell through getContent(2).  Content = [" + part.getClass().getName() + "]");
                  }
                }
            }
        }
        else if(content instanceof BASE64DecoderStream) {
            String      base64EncodedData;

            base64EncodedData = getContentFromInputStream(((BASE64DecoderStream)content));

            returnData.add(base64EncodedData);
        }
        else if(content instanceof StreamSource) {
            String      data;

            data = getContentFromInputStream(((StreamSource)content).getInputStream());

            returnData.add(data);
        }
        else {
            myLog("Fell through getContent(1).  Content = [" + content.getClass().getName() + "]");
        }

        return returnData;
    }

    private boolean contentShouldBeIgnored(String contentType) {
        int     semicolonIndex;

        semicolonIndex = contentType.indexOf(";");
        if(semicolonIndex > 0) {
            return (contentTypesToProcess.contains(contentType.substring(0, semicolonIndex).trim().toLowerCase()) ?
                false : true);
        }

        return true;
    }

    private String getContentFromInputStream(InputStream inputStream)
            throws IOException, MessagingException {
        byte[]          byteBuffer;
        int             bytesRead;
        int             totalBytesRead;
        StringBuffer    contentBuffer;

        byteBuffer = new byte[BYTE_BUFFER_SIZE];
        contentBuffer = new StringBuffer();
        totalBytesRead = 0;
        while((bytesRead = inputStream.read(byteBuffer)) > 0) {
            contentBuffer.append(new String(byteBuffer));
            totalBytesRead += bytesRead;
        }

        if(bytesRead > 0) {
            contentBuffer.append(new String(byteBuffer));
            totalBytesRead += bytesRead;
        }

        contentBuffer.setLength(totalBytesRead);

        return contentBuffer.toString();
    }

    private void writeValidationReportToReportDirectory(HL7Validator validator) throws IOException {
        writeDataToFile(reportDirectory, createResponseMessages(validator), validator,
                        Constants.REPORT_PREFIX,    Constants.REPORT_POSTFIX);
    }

    private void writeDataToProcessedDirectory(String msgData, HL7Validator validator)
            throws IOException {

        writeDataToFile(receivedMessageDirectory, msgData, validator, Constants.DATA_PREFIX,
                        (Parser.inputIsXML(msgData) ?
                                Constants.XML_DATA_POSTFIX : Constants.HL7_DATA_POSTFIX));
    }

    private void writeDataToHoldDirectory(String msgData, HL7Validator validator)
            throws IOException {

        writeDataToFile(holdDirectory, msgData, validator, Constants.DATA_PREFIX,
                        (Parser.inputIsXML(msgData) ? Constants.XML_DATA_POSTFIX : Constants.HL7_DATA_POSTFIX));
    }

    private void writeDataToFile(String directory, String msgData,
                                 HL7Validator validator, String  prefix, String       suffix)
            throws IOException {
        FileOutputStream    output;

        output = new FileOutputStream(directory + File.separator +
                                                generateFileName(validator, prefix, suffix));

        output.write(msgData.getBytes());

        output.close();
    }

    private String generateFileName(HL7Validator validator, String  prefix, String  suffix) {
        StringBuffer    fileName;

        fileName = new StringBuffer();

        fileName.append(prefix);
        fileName.append(validator.getMessageId());
        fileName.append("_");
        fileName.append(df.format(new Date()));
        fileName.append(suffix);

        return fileName.toString();
    }

    private String createResponseMessages(HL7Validator validator) {
        StringBuffer    buf = new StringBuffer();
        ArrayList       errorMessageList;
        Iterator        errorMessageIterator;

        errorMessageList = validator.getErrorMessages();

        if(errorMessageList.size() > 0) {
            buf.append("The HL7 data associated with message id: ");
            buf.append(validator.getMessageId());
            buf.append(" had the following errors and warnings:\n\n");

            errorMessageIterator =  errorMessageList.iterator();
            while(errorMessageIterator.hasNext()) {
                String      errorMessage = (String)errorMessageIterator.next();

                buf.append(errorMessage);
                buf.append("\n");
            }
        }
        else {
            buf.append("The HL7 data associated with message id: ");
            buf.append(validator.getMessageId());
            buf.append(" was successfully processed.\n");
        }

        return buf.toString();
    }

    private void createDirectories() throws MessagingException {
        File    dir;

        dir = new File(receivedMessageDirectory);
        if(dir.exists() == false) {
            dir.mkdirs();
        }

        dir = new File(holdDirectory);
        if(dir.exists() == false) {
            dir.mkdirs();
        }

        dir = new File(reportDirectory);
        if(dir.exists() == false) {
            dir.mkdirs();
        }

        return;
    }

    private boolean  isHL7Data(String  data) {
        String      trimmedData;

        trimmedData = data.trim();
        if(trimmedData.toUpperCase().indexOf("<MSH ") > -1 ||
                trimmedData.toUpperCase().indexOf("<MSH>") > -1 ||
                trimmedData.toUpperCase().startsWith("MSH")) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) throws IOException, MessagingException, HL7ValidatorException, JDOMException, PropertyAccessException {
        DirectConnectReader       reader;


        reader = new DirectConnectReader("directconnect.agilexhealth.com", "110",
                                         "receiver@directconnect.agilexhealth.com",       "receiver123",
                                         "/usr/tmp/processedMessages", "/usr/tmp/holdMessages", "/usr/tmp/validationReports",
                                         "../XML/SyndromicDataValidations.xml",
                                         "directconnect.agilexhealth.com",      "ralph@directconnect.agilexhealth.com",
                                         "ralph@directconnect.agilexhealth.com",
                                         "text/plain,text/xml,application/octet-stream",
                                         "/usr/tmp");
//        reader = new DirectConnectReader("172.16.45.49", "143", "ralph@directconnect.agilexhealth.com", "ralph123");

        reader.processIncommingEmail();
    }
}
