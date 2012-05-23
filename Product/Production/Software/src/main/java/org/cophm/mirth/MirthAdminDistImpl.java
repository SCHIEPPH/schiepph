/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cophm.mirth;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
/**
 *
 * @author dunnek
 */
public class MirthAdminDistImpl {
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());


    public String buildXMLMessageWithHeader(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewaySendAlertMessageType body) {

        String message = "";
        try
        {
            StringWriter writer = new StringWriter();

            QName qName = new QName("urn:gov:hhs:fha:nhinc:common:nhinccommonadapter","RespondingGateway_SendAlertMessage");
            JAXBElement element = new JAXBElement(qName,body.getClass(),body);


            javax.xml.bind.JAXBContext jaxbContext = javax.xml.bind.JAXBContext.newInstance(body.getClass());


            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal( element, writer);
            message = writer.toString();

            logger.info(message);






        }
        catch(Exception ex)
        {
            logger.error(ex.getMessage(), ex);
        }

       return message;
    }
}
