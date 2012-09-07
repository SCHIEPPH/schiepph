/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cophm.mirth;

import com.mirth.connect.connectors.ws.AcceptMessage;
import com.mirth.connect.connectors.ws.WebServiceMessageReceiver;
import org.cophm.util.PropertyAccessException;
import org.jdom.JDOMException;

import javax.ejb.Stateless;
import javax.jws.WebService;
import java.io.IOException;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "Adapter_AdministrativeDistribution", portName = "Adapter_AdministrativeDistribution_PortType", endpointInterface = "gov.hhs.fha.nhinc.adapteradmindistribution.AdapterAdministrativeDistributionPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapteradmindistribution", wsdlLocation = "wsdl/AdapterAdminDist.wsdl")
@javax.xml.ws.BindingType(value="http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")

@Stateless
public class MirthAdminDist extends AcceptMessage {
    private org.apache.log4j.Logger logger;

    public MirthAdminDist(WebServiceMessageReceiver webServiceMessageReceiver) {
       super(webServiceMessageReceiver);
       logger = this.getLogger();
    }
    public MirthAdminDist()
    {
        super(null);
        logger = this.getLogger();
    }

    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewaySendAlertMessageType body) throws JDOMException, PropertyAccessException, IOException {
        logger.info("begin sendAlertMessage()");

        String message = new MirthAdminDistImpl().buildXMLMessageWithHeader(body);
        this.webServiceMessageReceiver.processData(message);
    }
    protected org.apache.log4j.Logger getLogger()
    {
        return org.apache.log4j.Logger.getLogger(this.getClass());
    }



}
