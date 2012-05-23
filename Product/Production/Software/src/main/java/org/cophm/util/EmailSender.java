package org.cophm.util;

import java.io.IOException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    private Properties emailProperties;

    public EmailSender(String smtpHost, String toEmailAddress,
                       String fromEmailAddress, String adminEmailAddress) throws IOException {

        emailProperties = new Properties();
        emailProperties.put("mail.smtp.host", smtpHost);
        emailProperties.put("mail.to.email", toEmailAddress);
        emailProperties.put("mail.from.email", fromEmailAddress);
        emailProperties.put("mail.admin.email", adminEmailAddress);

    }

    public void sendEmail(String recipientEmailAddress, String  subject, String messageText) {
        Session               session;
        MimeMessage           message;
        Multipart             multiPartMessage;
        MimeBodyPart          emailBody;
        InternetAddress[]     address;

        session = Session.getInstance(emailProperties, null);
        session.setDebug(false);

        if(messageText == null) {
            messageText = "";
        }

        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String)emailProperties.get("mail.from.email")));

            address = new InternetAddress[1];
            address[0] = new InternetAddress(recipientEmailAddress);

            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());

            emailBody = new MimeBodyPart();
            emailBody.setText(messageText);

            multiPartMessage = new MimeMultipart();
            multiPartMessage.addBodyPart(emailBody);

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
