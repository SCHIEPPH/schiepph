package org.cophm.util;

import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    private Properties emailProperties;

    public EmailSender(String smtpHost, String fromEmailAddress, String adminEmailAddress)
            throws IOException {

        //
        // email property names:
        //      mail.smtp.host
        //      mail.to.email
        //      mail.from.email
        //      mail.admin.email
        //
        // Set some default values.
        //
        emailProperties = new Properties();
        emailProperties.put("mail.smtp.host", smtpHost);
        emailProperties.put("mail.from.email", fromEmailAddress);
        emailProperties.put("mail.admin.email", adminEmailAddress);

    }

    public void sendEmail(String recipientEmailAddress, String  subject, String messageText) {
        String[]    messageTextArray = new String[1];

        messageTextArray[0] = messageText;

        sendEmail(recipientEmailAddress, subject, messageTextArray);
    }

    public void sendEmail(String recipientEmailAddress, String  subject, String[] attachments) {

        sendEmail(recipientEmailAddress, subject, " ", attachments);
    }

    public void sendEmail(String recipientEmailAddress, String  subject, String  messageText, String[] attachments) {
        Session               session;
        MimeMessage           message;
        Multipart             multiPartMessage;
        MimeBodyPart          emailBody;
        InternetAddress[]     address;

        session = Session.getInstance(emailProperties, null);
        session.setDebug(false);

        if(attachments == null) {
            attachments = new String[1];
            attachments[0] = "";
        }

        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String)emailProperties.get("mail.from.email")));
            message.setText(messageText);

            address = new InternetAddress[1];
            address[0] = new InternetAddress(recipientEmailAddress);

            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());

            multiPartMessage = new MimeMultipart();
            for(int  x = 0; x < attachments.length; x++) {
                emailBody = new MimeBodyPart();
                emailBody.setText(attachments[x]);

                multiPartMessage.addBodyPart(emailBody);
            }

            message.setContent(multiPartMessage);

            Transport.send(message);
        }
        catch(MessagingException mex) {
            mex.printStackTrace();
            Exception     ex = null;
            if((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }

    }
}
