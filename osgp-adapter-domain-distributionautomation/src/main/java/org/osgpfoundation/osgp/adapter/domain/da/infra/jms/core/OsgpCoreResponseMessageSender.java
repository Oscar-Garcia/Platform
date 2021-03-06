/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgpfoundation.osgp.adapter.domain.da.infra.jms.core;

import com.alliander.osgp.shared.infra.jms.Constants;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.ObjectMessage;

// This class should send response messages to OSGP Core.
@Component(value = "domainDistributionAutomationOutgoingOsgpCoreResponseMessageSender")
public class OsgpCoreResponseMessageSender {

    @Autowired
    @Qualifier("domainDistributionAutomationOutgoingOsgpCoreResponsesJmsTemplate")
    private JmsTemplate osgpCoreResponsesJmsTemplate;

    public void send(final ResponseMessage responseMessage, final String messageType) {

        this.osgpCoreResponsesJmsTemplate.send(session -> {
            final ObjectMessage objectMessage = session.createObjectMessage();
            objectMessage.setJMSType(messageType);
            objectMessage.setStringProperty(Constants.ORGANISATION_IDENTIFICATION,
                    responseMessage.getOrganisationIdentification());
            objectMessage.setStringProperty(Constants.DEVICE_IDENTIFICATION,
                    responseMessage.getDeviceIdentification());
            objectMessage.setObject(responseMessage);
            return objectMessage;
        });
    }
}
